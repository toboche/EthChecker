package pl.toboche.ethchecker.erc20balance

import io.reactivex.Flowable
import pl.toboche.ethchecker.base.BasePresenter
import pl.toboche.ethchecker.erc20balance.ui.Erc20TokenBalanceDescription

class Erc20BalanceContract {
    abstract class Presenter : BasePresenter<View>()

    interface View {
        fun getUserInput(): Flowable<CharSequence>
        fun showBalanceDescriptions(descriptions: List<Erc20TokenBalanceDescription>)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }
}