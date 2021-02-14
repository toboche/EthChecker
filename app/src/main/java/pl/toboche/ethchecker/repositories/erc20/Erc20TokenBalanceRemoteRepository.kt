package pl.toboche.ethchecker.repositories.erc20

import io.reactivex.Single
import pl.toboche.ethchecker.base.ConfigurationConstants
import pl.toboche.ethchecker.repositories.balance.BalanceResponse
import pl.toboche.ethchecker.repositories.balance.BalanceRetrofitService
import java.util.*
import javax.inject.Inject

class Erc20TokenBalanceRemoteRepository @Inject constructor(
    private val erc20TokenListRetrofitService: Erc20TokenListRetrofitService,
    private val balanceRetrofitService: BalanceRetrofitService,
    private val configurationConstants: ConfigurationConstants
) : Erc20TokenBalanceRepository {

    private var cachedTokens: TokensResponse? = null

    override fun getTokenBalancesWithNameContaining(searchText: String): Single<List<Erc20TokenBalance>> {
        return getCachedTokens()
            .map { it.tokenModels }
            .flattenAsFlowable { it }
            .filter { symbolContainsSearchText(it, searchText) }
            .flatMapSingle { token ->
                balanceRetrofitService.getErc20TokenBalance(
                    address = configurationConstants.ethereumAddress,
                    contractAddress = token.address
                ).map { mapToTokenBalance(token, it) }
            }
            .toList()
    }

    private fun getCachedTokens(): Single<TokensResponse> =
        if (cachedTokens == null) {
            erc20TokenListRetrofitService.getTokens()
                .doOnSuccess { cachedTokens = it }
        } else {
            Single.just(cachedTokens)
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