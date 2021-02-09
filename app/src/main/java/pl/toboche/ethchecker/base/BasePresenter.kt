package pl.toboche.ethchecker.base

abstract class BasePresenter<View> {
    var view: View? = null

    open fun attach(view: View) {
        this.view = view
    }

    open fun detach() {
        view = null
    }
}