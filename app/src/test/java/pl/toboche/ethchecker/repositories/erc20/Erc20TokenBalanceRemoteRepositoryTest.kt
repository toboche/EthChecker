package pl.toboche.ethchecker.repositories.erc20

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Test
import pl.toboche.ethchecker.base.ConfigurationConstants
import pl.toboche.ethchecker.repositories.balance.BalanceResponse
import pl.toboche.ethchecker.repositories.balance.BalanceRetrofitService

class Erc20TokenBalanceRemoteRepositoryTest {

    val mockResponseToken = TokenModel("someAddressHere", "SYM")
    val mockErc20TokenListRetrofitService: Erc20TokenListRetrofitService = mock {
        on {
            getTokens(
                any(),
                any()
            )
        } doReturn Single.just(TokensResponse(listOf(mockResponseToken)))
    }

    val mockBalanceResponse = BalanceResponse(
        "someStatusHere",
        "messageHere",
        "resultHere"
    )
    val mockBalanceRetrofitService: BalanceRetrofitService = mock {
        on { getErc20TokenBalance(any(), any()) } doReturn Single.just(mockBalanceResponse)
    }

    val systemUnderTest = Erc20TokenBalanceRemoteRepository(
        mockErc20TokenListRetrofitService,
        mockBalanceRetrofitService
    )

    val mockUserQuery = "queryHere"
    val usdtTokenModel = TokenModel(
        address = "some address of USDT",
        symbol = "USDT"
    )
    val uniTokenModel = TokenModel(
        address = "address of UNI",
        symbol = "UNI"
    )

    @Test
    fun `cache the erc20 token list request`() {
        systemUnderTest.getTokenBalancesWithNameContaining(mockUserQuery)
            .test()

        systemUnderTest.getTokenBalancesWithNameContaining(mockUserQuery)
            .test()

        verify(mockErc20TokenListRetrofitService).getTokens(any(), any())
        verifyNoMoreInteractions(mockErc20TokenListRetrofitService)
    }

    @Test
    fun `do not cache the erc20 token list request when it failed`() {
        givenTokensRequestWillFail()
        systemUnderTest.getTokenBalancesWithNameContaining(mockUserQuery)
            .test()

        givenTokensWillBeReturned(mockResponseToken, mockResponseToken)
        systemUnderTest.getTokenBalancesWithNameContaining(mockUserQuery)
            .test()

        verify(mockErc20TokenListRetrofitService, times(2)).getTokens(any(), any())
        verifyNoMoreInteractions(mockErc20TokenListRetrofitService)
    }

    @Test
    fun `return balance for token matching user query`() {
        val userQuery = "USD"
        val matchingTokenModel = usdtTokenModel
        val notMatchingTokenModel = uniTokenModel
        val expectedBalance = "432"
        givenTokensWillBeReturned(matchingTokenModel, notMatchingTokenModel)
        givenBalanceWillBeReturnedForToken(
            matchingTokenModel,
            expectedBalance
        )
        val systemUnderTest = Erc20TokenBalanceRemoteRepository(
            mockErc20TokenListRetrofitService,
            mockBalanceRetrofitService
        )

        systemUnderTest.getTokenBalancesWithNameContaining(userQuery)
            .test()
            .assertValues(
                listOf(
                    Erc20TokenBalance(
                        matchingTokenModel.symbol,
                        expectedBalance
                    )
                )
            )

        verify(mockBalanceRetrofitService).getErc20TokenBalance(
            address = ConfigurationConstants.ETHEREUM_ADDRESS,
            contractAddress = matchingTokenModel.address
        )
        verifyNoMoreInteractions(mockBalanceRetrofitService)
    }

    private fun givenBalanceWillBeReturnedForToken(
        matchingTokenModel: TokenModel,
        expectedBalance: String
    ) {
        whenever(
            mockBalanceRetrofitService.getErc20TokenBalance(
                address = ConfigurationConstants.ETHEREUM_ADDRESS,
                contractAddress = matchingTokenModel.address
            )
        )
            .thenReturn(
                Single.just(
                    BalanceResponse(
                        status = "mock status",
                        message = "msg",
                        result = expectedBalance
                    )
                )
            )
    }

    private fun givenTokensWillBeReturned(
        matchingTokenModel: TokenModel,
        notMatchingTokenModel: TokenModel
    ) {
        whenever(mockErc20TokenListRetrofitService.getTokens(any(), any()))
            .thenReturn(
                Single.just(
                    TokensResponse(
                        listOf(
                            matchingTokenModel,
                            notMatchingTokenModel
                        )
                    )
                )
            )
    }

    private fun givenTokensRequestWillFail() {
        whenever(mockErc20TokenListRetrofitService.getTokens(any(), any()))
            .thenReturn(
                Single.error(NullPointerException())
            )
    }
}