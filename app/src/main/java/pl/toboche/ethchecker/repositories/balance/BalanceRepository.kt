package pl.toboche.ethchecker.repositories.balance

import io.reactivex.Single

interface BalanceRepository {
    fun getAccountBalance(address: String): Single<AccountBalance>
}