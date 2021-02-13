package pl.toboche.ethchecker.repositories.price

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class CryptoPriceRetrofitServiceTest {

    val server = MockWebServer().apply {
        start()
    }
    val systemUnderTest =
        ProvidedPriceModule(server.url("/").toString()).providePriceRetrofitService(
            OkHttpClient()
        )

    val mockIds = "expectedIds"
    val mockCurrencies = "currencies"

    @Before
    fun setUp() {
        server.enqueue(
            MockResponse().setBody(
                "{\"ethereum\":{\"usd\":1845.8}}"
            )
        )
    }

    @Test
    fun `make valid request`() {
        val expectedIds = mockIds
        val expectedCurrencies = mockCurrencies

        systemUnderTest.getPrice(
            expectedIds,
            expectedCurrencies
        )
            .test()

        val request = server.takeRequest()

        assertThat(request.path).isEqualTo(
            "/api/v3/simple/price?" +
                    "ids=$expectedIds&" +
                    "vs_currencies=$expectedCurrencies"
        )
    }

    @Test
    fun `pass valid default params`() {
        val expectedIds = "ethereum"
        val expectedCurrencies = "usd"

        systemUnderTest.getPrice()
            .test()

        val request = server.takeRequest()

        assertThat(request.path).isEqualTo(
            "/api/v3/simple/price?" +
                    "ids=$expectedIds&" +
                    "vs_currencies=$expectedCurrencies"
        )
    }

    @Test
    fun `pass response value`() {
        systemUnderTest.getPrice(
            mockIds,
            mockCurrencies
        )
            .test()
            .assertValue(
                PriceResponse(
                    CryptoPrice(
                        1845.8
                    )
                )
            )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}