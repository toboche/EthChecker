package pl.toboche.ethchecker.home.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import pl.toboche.ethchecker.R
import pl.toboche.ethchecker.base.MainActivity
import pl.toboche.ethchecker.home.HomeContract
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeContract.View {

    @Inject
    lateinit var presenter: HomeContract.Presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
    }

    override fun onDestroyView() {
        presenter.detach()
        super.onDestroyView()
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

    override fun navigateToErc20Screen() {
        //in a production app this could be done in some form of a navigator
        (requireActivity() as MainActivity).showErc20BalanceScreen()
    }

    override fun setErc20BalanceButtonAction(function: () -> Unit) {
        requireView().findViewById<Button>(R.id.erc_tokens_balance_button).setOnClickListener {
            function()
        }
    }
}