package pl.toboche.ethchecker.repositories.balance

import io.reactivex.Single
import pl.toboche.ethchecker.repositories.price.CryptoPriceRetrofitService
import javax.inject.Inject

class BalanceRemoteRepository @Inject constructor(
    private val balanceRetrofitService: BalanceRetrofitService,
    private val cryptoPriceRetrofitService: CryptoPriceRetrofitService
) : BalanceRepository {

    companion object {
        val WEI_TO_ETH_MULTIPLIER = 1_000_000_000_000_000_000.toBigDecimal()
    }

    override fun getAccountBalance(address: String): Single<AccountBalance> =
        Single.zip(
            getBalanceInEth(address),
            getEthPriceInDollars(),
            { balanceInEth, price ->
                AccountBalance(balanceInEth, balanceInEth * price)
            }
        )

    private fun getBalanceInEth(address: String) = balanceRetrofitService.getBalance(address)
        .map { it.result.toBigDecimal().divide(WEI_TO_ETH_MULTIPLIER) }

    private fun getEthPriceInDollars() = cryptoPriceRetrofitService.getPrice()
        .map { it.ethereum.usd.toBigDecimal() }
}
