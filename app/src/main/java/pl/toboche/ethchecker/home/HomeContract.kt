package pl.toboche.ethchecker.home

import pl.toboche.ethchecker.base.BasePresenter

class HomeContract {
    abstract class Presenter : BasePresenter<View>()

    interface View {
        fun showAddress(ethereumAddress: String)
        fun showBalance(ethereumAddress: String)
        fun showError(errorText: String)
        fun showProgress()
        fun hideProgress()
    }
}

