package pl.toboche.ethchecker.repositories.balance

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Test
import pl.toboche.ethchecker.repositories.price.CryptoPrice
import pl.toboche.ethchecker.repositories.price.CryptoPriceRetrofitService
import pl.toboche.ethchecker.repositories.price.PriceResponse


class BalanceRemoteRepositoryTest {
    val mockEthPriceInDollars = 22.3
    val mockEthBalanceString = "10000000000000000000"
    val mockEthBalanceBigDecimal = 10.toBigDecimal()
    val mockBalanceResponse = BalanceResponse(
        status = "OK",
        message = "some message here",
        result = mockEthBalanceString
    )
    val mockBalanceRetrofitService: BalanceRetrofitService = mock {
        on { getBalance(any()) } doReturn Single.just(
            mockBalanceResponse
        )
    }

    val mockPriceResponse = PriceResponse(
        CryptoPrice(
            mockEthPriceInDollars
        )
    )
    val mockCryptoPriceRetrofitService: CryptoPriceRetrofitService = mock {
        on { getPrice(any(), any()) } doReturn Single.just(mockPriceResponse)
    }

    val mockError = Exception()
    val mockAddress = "mockAddress"

    val systemUnderTest = BalanceRemoteRepository(
        mockBalanceRetrofitService,
        mockCryptoPriceRetrofitService
    )

    @Test
    fun `calculate proper account balance`() {
        systemUnderTest.getAccountBalance(mockAddress)
            .test()
            .assertResult(
                AccountBalance(
                    mockEthBalanceBigDecimal,
                    mockEthBalanceBigDecimal * mockEthPriceInDollars.toBigDecimal()
                )
            )
            .assertComplete()
    }

    @Test
    fun `pass correct values when requesting data`() {
        systemUnderTest.getAccountBalance(mockAddress)
            .test()

        verify(mockBalanceRetrofitService).getBalance(mockAddress)
        verify(mockCryptoPriceRetrofitService).getPrice()
    }

    @Test
    fun `handle errors when requesting balance`() {
        whenever(mockBalanceRetrofitService.getBalance(any()))
            .thenReturn(Single.error(mockError))

        systemUnderTest.getAccountBalance(mockAddress)
            .test()
            .assertError(mockError)
    }

    @Test
    fun `handle errors when requesting price`() {
        whenever(mockCryptoPriceRetrofitService.getPrice(any(), any()))
            .thenReturn(Single.error(mockError))

        systemUnderTest.getAccountBalance(mockAddress)
            .test()
            .assertError(mockError)
    }
}