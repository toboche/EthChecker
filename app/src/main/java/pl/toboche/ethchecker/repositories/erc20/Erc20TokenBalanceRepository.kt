package pl.toboche.ethchecker.repositories.erc20

import io.reactivex.Single

interface Erc20TokenBalanceRepository {
    //If the loading logic I'd consider wrapping the result in a Success/Failure sealed class
    //so that the stream doesn't finish on any error and the result of the error can be more
    //self-explanatory
    fun getTokenBalancesWithNameContaining(searchText: String): Single<List<Erc20TokenBalance>>
}