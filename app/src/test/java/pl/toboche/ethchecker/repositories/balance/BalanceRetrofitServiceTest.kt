package pl.toboche.ethchecker.repositories.balance

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test


class BalanceRetrofitServiceTest {

    val server = MockWebServer().apply {
        start()
    }
    val systemUnderTest =
        ProvidedBalanceModule(server.url("/").toString()).provideBalanceRetrofitService(
            OkHttpClient()
        )

    val mockStatus = "1"
    val mockMessage = "OK"
    val mockResult = "1982888316810680043"
    val mockAddress = "someAddressHere"

    @Before
    fun setUp() {
        enqueueServerResponse()
    }

    @Test
    fun `make valid request with default param values`() {
        val expectedAddress = mockAddress
        val expectedModule = "account"
        val expectedAction = "balance"
        val expectedTag = "latest"
        val expectedApiKey = "28VA2X5T639Q4V9I5J2TWMIZT4JMX3IQGQ"

        systemUnderTest.getBalance(
            mockAddress
        )
            .test()

        val request = server.takeRequest()

        assertThat(request.path).isEqualTo(
            "/api?" +
                    "module=$expectedModule&" +
                    "action=$expectedAction&" +
                    "tag=$expectedTag&" +
                    "apiKey=$expectedApiKey&" +
                    "address=$expectedAddress"
        )
    }

    @Test
    fun `pass response values`() {
        val expectedStatus = mockStatus
        val expectedResult = mockResult
        val expectedMessage = mockMessage

        systemUnderTest.getBalance(mockAddress)
            .test()
            .assertValue(
                BalanceResponse(
                    status = expectedStatus,
                    message = expectedMessage,
                    result = expectedResult
                )
            )
    }

    private fun enqueueServerResponse() {
        server.enqueue(
            MockResponse().setBody(
                "{\n" +
                        "\"status\": \"$mockStatus\",\n" +
                        "\"message\": \"$mockMessage\",\n" +
                        "\"result\": \"$mockResult\"\n" +
                        "}"
            )
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}