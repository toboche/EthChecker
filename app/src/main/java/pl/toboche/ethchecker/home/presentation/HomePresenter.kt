package pl.toboche.ethchecker.home.presentation

import pl.toboche.ethchecker.base.scheduler.ApplicationScheduler
import pl.toboche.ethchecker.home.HomeContract
import pl.toboche.ethchecker.repositories.balance.AccountBalance
import pl.toboche.ethchecker.repositories.balance.BalanceRepository
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val applicationScheduler: ApplicationScheduler
) : HomeContract.Presenter() {

    companion object {
        const val ETHEREUM_ADDRESS = "0xde57844f758a0a6a1910a4787ab2f7121c8978c3"
        const val LAST_DIGITS_TO_TAKE = 4
        const val FIRST_DIGITS_TO_TAKE = 6
        val currencyInstance: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val ethFormat = DecimalFormat("#0.00").apply {
            isDecimalSeparatorAlwaysShown = true
        }
    }

    override fun attach(view: HomeContract.View) {
        super.attach(view)
        val addressToShow = ETHEREUM_ADDRESS.take(FIRST_DIGITS_TO_TAKE) +
                "..." +
                ETHEREUM_ADDRESS.takeLast(LAST_DIGITS_TO_TAKE)
        view.showAddress(addressToShow)
        applicationScheduler.schedule(
            balanceRepository.getAccountBalance(ETHEREUM_ADDRESS),
            { showBalance(view, currencyInstance, it, ethFormat) },
            { showError(view) },
            this
        )
    }

    private fun showError(view: HomeContract.View) =
        view.showError("Error loading data")

    private fun showBalance(
        view: HomeContract.View,
        currencyInstance: NumberFormat,
        it: AccountBalance,
        ethFormat: DecimalFormat
    ) = view.showBalance(
        "${currencyInstance.format(it.dollars)} (${ethFormat.format(it.ethereum)}) ETH"
    )

    override fun detach() {
        applicationScheduler.dispose(this)
        super.detach()
    }
}
