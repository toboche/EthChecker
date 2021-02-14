package pl.toboche.ethchecker.repositories.erc20

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Erc20TokenListRetrofitService {
    @GET("getTopTokens")
    fun getTokens(
        @Query("limit")
        limit: Int = 100,
        @Query("apiKey")
        apiKey: String = "freekey"
    ): Single<TokensResponse>
}