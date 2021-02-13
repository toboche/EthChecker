package pl.toboche.ethchecker.repositories.price

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoPriceRetrofitService {
    @GET("api/v3/simple/price")
    fun getPrice(
        @Query("ids")
        ids: String = "ethereum",
        @Query("vs_currencies")
        currencies: String = "usd",
    ): Single<PriceResponse>
}
