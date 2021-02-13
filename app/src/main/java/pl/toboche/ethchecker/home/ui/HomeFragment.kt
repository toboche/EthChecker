package pl.toboche.ethchecker.home.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
        requireView().findViewById<TextView>(R.id.address_text).text = ethereumAddress
    }

    override fun showBalance(ethereumAddress: String) {
        requireView().findViewById<TextView>(R.id.balance_text).text = ethereumAddress
    }

    override fun showError(errorText: String) {
        Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        requireView().findViewById<ProgressBar>(R.id.home_progress_bar).visibility = View.VISIBLE

    }

    override fun hideProgress() {
        requireView().findViewById<ProgressBar>(R.id.home_progress_bar).visibility = View.INVISIBLE
    }
}