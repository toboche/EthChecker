package pl.toboche.ethchecker.base.scheduler

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultScheduler @Inject constructor(
    override val observingScheduler: Scheduler,
    override val executingScheduler: Scheduler
) : ApplicationScheduler {

    private val subscriptions = HashMap<Any, MutableList<Disposable>>()

    override fun <T> schedule(
        single: Single<T>,
        successfulAction: (T) -> Unit,
        errorAction: (Throwable) -> Unit,
        subscriber: Any
    ) {
        if (!subscriptions.containsKey(subscriber)) {
            subscriptions[subscriber] = mutableListOf()
        }
        subscriptions[subscriber]!!.add(
            single
                .onTerminateDetach()
                .observeOn(observingScheduler)
                .subscribeOn(executingScheduler)
                .subscribe(successfulAction, errorAction)
        )
    }

    override fun <T> schedule(
        flowable: Flowable<T>,
        onNextAction: (T) -> Unit,
        errorAction: (Throwable) -> Unit,
        subscriber: Any
    ) {
        if (!subscriptions.containsKey(subscriber)) {
            subscriptions[subscriber] = mutableListOf()
        }
        subscriptions[subscriber]!!.add(
            flowable
                .onTerminateDetach()
                .observeOn(observingScheduler)
                .subscribeOn(executingScheduler)
                .subscribe(onNextAction, errorAction)
        )
    }

    override fun dispose(subscriber: Any) {
        subscriptions[subscriber]
            ?: emptyList<Disposable>()
                .forEach { it.dispose() }

        subscriptions.remove(subscriber)
    }
}