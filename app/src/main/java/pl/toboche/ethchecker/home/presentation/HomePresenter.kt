package pl.toboche.ethchecker.home.presentation

import pl.toboche.ethchecker.home.HomeContract
import javax.inject.Inject

class HomePresenter @Inject constructor(

) : HomeContract.Presenter() {

    companion object {
        const val ETHEREUM_ADDRESS = "0xde57844f758a0a6a1910a4787ab2f7121c8978c3"
        const val LAST_DIGITS_TO_TAKE = 4
        const val FIRST_DIGITS_TO_TAKE = 6
    }

    override fun attach(view: HomeContract.View) {
        super.attach(view)
        val addressToShow = ETHEREUM_ADDRESS.take(FIRST_DIGITS_TO_TAKE) +
                "..." +
                ETHEREUM_ADDRESS.takeLast(LAST_DIGITS_TO_TAKE)
        view.showAddress(addressToShow)
    }
}
