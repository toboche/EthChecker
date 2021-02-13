package pl.toboche.ethchecker.erc20balance.ui

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import pl.toboche.ethchecker.R
import pl.toboche.ethchecker.erc20balance.Erc20BalanceContract
import javax.inject.Inject

@AndroidEntryPoint
class Erc20BalanceFragment : Fragment(R.layout.fragment_erc20_balance), Erc20BalanceContract.View {

    @Inject
    lateinit var presenter: Erc20BalanceContract.Presenter

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach()
        super.onPause()
    }

}