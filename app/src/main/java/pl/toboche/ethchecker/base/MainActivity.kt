package pl.toboche.ethchecker.base

import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import pl.toboche.ethchecker.R
import pl.toboche.ethchecker.erc20balance.ui.Erc20BalanceFragment

@AndroidEntryPoint
class MainActivity : FragmentActivity(R.layout.activity_main) {
    fun showErc20BalanceScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, Erc20BalanceFragment())
            .addToBackStack(null)
            .commit()
    }
}