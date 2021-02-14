package pl.toboche.ethchecker.repositories.erc20

import io.reactivex.Single
import pl.toboche.ethchecker.base.ConfigurationConstants.ETHEREUM_ADDRESS
import pl.toboche.ethchecker.repositories.balance.BalanceResponse
import pl.toboche.ethchecker.repositories.balance.BalanceRetrofitService
import java.util.*
import javax.inject.Inject

class Erc20TokenBalanceRemoteRepository @Inject constructor(
    erc20TokenListRetrofitService: Erc20TokenListRetrofitService,
    private val balanceRetrofitService: BalanceRetrofitService
) : Erc20TokenBalanceRepository {

    private val cachedTokens = erc20TokenListRetrofitService.getTokens()
        .cache()

    override fun getTokenBalancesWithNameContaining(searchText: String): Single<List<Erc20TokenBalance>> {
        return cachedTokens
            .map { it.tokenModels }
            .flattenAsFlowable { it }
            .filter { symbolContainsSearchText(it, searchText) }
            .flatMapSingle { token ->
                balanceRetrofitService.getErc20TokenBalance(
                    address = ETHEREUM_ADDRESS,
                    contractAddress = token.address
                ).map { mapToTokenBalance(token, it) }
            }
            .toList()
    }

    private fun mapToTokenBalance(
        tokenModel: TokenModel,
        balanceResponse: BalanceResponse
    ) = Erc20TokenBalance(
        symbol = tokenModel.symbol,
        balance = balanceResponse.result,
    )

    private fun symbolContainsSearchText(
        it: TokenModel,
        searchText: String
    ) = it.symbol.toUpperCase(Locale.ROOT).contains(
        searchText.toUpperCase(
            Locale.ROOT
        )
    )
}