package pl.toboche.ethchecker.erc20balance.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.toboche.ethchecker.R

class Erc20BalanceRecyclerAdapter(
    private val context: Context,
    private var balances: List<Erc20TokenBalanceDescription> = emptyList()
) : RecyclerView.Adapter<Erc20BalanceRecyclerAdapter.BalanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.item_erc_20_balance,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.setBalance(balances[position])
    }

    override fun getItemCount() = balances.size

    fun setBalanceDescriptions(newBalances: List<Erc20TokenBalanceDescription>) {
        balances = newBalances
        notifyDataSetChanged()
    }

    class BalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var title: TextView = itemView.findViewById(R.id.item_balance_title)

        fun setBalance(erc20TokenBalanceDescription: Erc20TokenBalanceDescription) {
            title.text = erc20TokenBalanceDescription.description
        }
    }
}

