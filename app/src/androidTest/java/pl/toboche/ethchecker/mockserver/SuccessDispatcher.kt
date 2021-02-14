package pl.toboche.ethchecker.mockserver

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import pl.toboche.ethchecker.mockserver.AssetReaderUtil.asset

class SuccessDispatcher(
    private val context: Context = InstrumentationRegistry.getInstrumentation().context
) : Dispatcher() {
    private val responseFilesByPath: Map<String, String> = mapOf(
        APIPaths.ETHERSCAN_API_ETH_BALANCE to MockFiles.ETHERSCAN_API_ETH_BALANCE,
        APIPaths.COINGECKO_API_ETH_PRICE to MockFiles.COINGECKO_API_ETH_PRICE,
    )

    override fun dispatch(request: RecordedRequest): MockResponse {
        val errorResponse = MockResponse().setResponseCode(404)

        val responseFile = responseFilesByPath[request.path]

        return if (responseFile != null) {
            val responseBody = asset(context, responseFile)
            MockResponse().setResponseCode(200).setBody(responseBody)
        } else {
            errorResponse
        }
    }
}