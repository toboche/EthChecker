package pl.toboche.ethchecker.base.scheduler

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single

interface ApplicationScheduler {

    val observingScheduler: Scheduler
    val executingScheduler: Scheduler

    fun <T> schedule(
        single: Single<T>,
        successfulAction: (T) -> Unit,
        errorAction: (Throwable) -> Unit,
        subscriber: Any
    )

    fun <T> schedule(
        flowable: Flowable<T>,
        onNextAction: (T) -> Unit,
        errorAction: (Throwable) -> Unit,
        subscriber: Any
    )

    fun dispose(subscriber: Any)
}