package pl.toboche.ethchecker.base.scheduler

import io.reactivex.Single

interface ApplicationScheduler {
    fun <T> schedule(
        single: Single<T>,
        successfulAction: (T) -> Unit,
        errorAction: (Throwable) -> Unit,
        subscriber: Any
    )

    fun dispose(subscriber: Any)
}