package pl.toboche.ethchecker.repositories.erc20

import io.reactivex.Single

interface Erc20TokenBalanceRepository {
    fun getTokenBalancesWithNameContaining(searchText: String): Single<List<Erc20TokenBalance>>
}