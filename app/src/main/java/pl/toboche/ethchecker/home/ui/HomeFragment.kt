package pl.toboche.ethchecker.home.ui

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import pl.toboche.ethchecker.R
import pl.toboche.ethchecker.home.HomeContract
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeContract.View {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach()
        super.onPause()
    }

    override fun showAddress(ethereumAddress: String) {
        TODO("Not yet implemented")
    }
}