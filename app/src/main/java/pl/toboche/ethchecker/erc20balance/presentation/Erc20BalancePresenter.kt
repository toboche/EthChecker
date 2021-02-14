package pl.toboche.ethchecker.erc20balance.presentation

import io.reactivex.Single
import pl.toboche.ethchecker.base.scheduler.ApplicationScheduler
import pl.toboche.ethchecker.erc20balance.Erc20BalanceContract
import pl.toboche.ethchecker.erc20balance.ui.Erc20TokenBalanceDescription
import pl.toboche.ethchecker.repositories.erc20.Erc20TokenBalance
import pl.toboche.ethchecker.repositories.erc20.Erc20TokenBalanceRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Erc20BalancePresenter @Inject constructor(
    private val scheduler: ApplicationScheduler,
    private val erc20TokenBalanceRepository: Erc20TokenBalanceRepository
) : Erc20BalanceContract.Presenter() {

    override fun attach(view: Erc20BalanceContract.View) {
        super.attach(view)
        subscribeToUserQueryUpdates(view)
    }

    private fun subscribeToUserQueryUpdates(view: Erc20BalanceContract.View) {
        scheduler.schedule(
            flowable = view.getUserInput()
                .filter { it.length > 1 }
                .debounce(300, TimeUnit.MILLISECONDS, scheduler.executingScheduler)
                .observeOn(scheduler.observingScheduler)
                .doOnNext { view.showProgress() }
                .observeOn(scheduler.executingScheduler)
                .switchMapSingle {
                    erc20TokenBalanceRepository.getTokenBalancesWithNameContaining(it.toString())
                        .map { mapToListOfBalanceDescriptions(it) }
                        .observeOn(scheduler.observingScheduler)
                        .doOnError { onErrorRefreshingList(view) }
                        .onErrorResumeNext(Single.just(emptyList()))
                },
            { onListRefreshingComplete(view, it) },
            { onErrorRefreshingList(view) },
            this
        )
    }

    private fun mapToListOfBalanceDescriptions(it: List<Erc20TokenBalance>) =
        it.map {
            Erc20TokenBalanceDescription(
                "${it.symbol} Balance: ${it.balance} ${it.symbol}"
            )
        }

    private fun onListRefreshingComplete(
        view: Erc20BalanceContract.View,
        descriptions: List<Erc20TokenBalanceDescription>
    ) {
        view.hideProgress()
        view.showBalanceDescriptions(descriptions)
    }

    private fun onErrorRefreshingList(view: Erc20BalanceContract.View) {
        view.hideProgress()
        view.showError("Error refreshing the list")
    }

    override fun detach() {
        scheduler.dispose(this)
        super.detach()
    }
}