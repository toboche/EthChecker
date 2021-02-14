package pl.toboche.ethchecker.erc20balance.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import pl.toboche.ethchecker.R
import pl.toboche.ethchecker.erc20balance.Erc20BalanceContract
import javax.inject.Inject

@AndroidEntryPoint
class Erc20BalanceFragment : Fragment(R.layout.fragment_erc20_balance), Erc20BalanceContract.View {

    @Inject
    lateinit var presenter: Erc20BalanceContract.Presenter

    private var erc20BalanceRecyclerAdapter: Erc20BalanceRecyclerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        erc20BalanceRecyclerAdapter = Erc20BalanceRecyclerAdapter(requireActivity())
        view.findViewById<RecyclerView>(R.id.erc_20_balance_list)
            .adapter = erc20BalanceRecyclerAdapter
    }

    override fun onDestroyView() {
        presenter.detach()
        super.onDestroyView()
    }

    override fun getUserInput(): Flowable<CharSequence> =
        requireView()
            .findViewById<EditText>(R.id.search_for_and_erc20_token_edit_text)
            .textChanges()
            .toFlowable(BackpressureStrategy.LATEST)

    override fun showBalanceDescriptions(descriptions: List<Erc20TokenBalanceDescription>) {
        erc20BalanceRecyclerAdapter!!.setBalanceDescriptions(descriptions)
    }

    override fun showProgress() {
        requireView()
            .findViewById<ProgressBar>(R.id.erc_20_progress_bar).visibility = View.VISIBLE
    }

    override fun hideProgress() {
        requireView()
            .findViewById<ProgressBar>(R.id.erc_20_progress_bar).visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

}