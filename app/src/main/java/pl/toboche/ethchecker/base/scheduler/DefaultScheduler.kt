package pl.toboche.ethchecker.base.scheduler

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultScheduler @Inject constructor(
    private val observingScheduler: Scheduler,
    private val executingScheduler: Scheduler
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

    override fun dispose(subscriber: Any) {
        subscriptions[subscriber]
            ?: emptyList<Disposable>()
                .forEach { it.dispose() }

        subscriptions.remove(subscriber)
    }
}