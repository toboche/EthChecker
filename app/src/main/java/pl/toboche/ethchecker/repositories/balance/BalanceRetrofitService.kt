package pl.toboche.ethchecker.repositories.balance

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BalanceRetrofitService {
    @GET("api")
    //One might want to inject these into the using service, depending on the future usage
    fun getBalance(
        @Query("address")
        address: String,
        @Query("module")
        module: String = "account",
        @Query("action")
        action: String = "balance",
        @Query("tag")
        tag: String = "latest",
        //This could be injected using a @Named String
        @Query("apiKey")
        apiKey: String = "28VA2X5T639Q4V9I5J2TWMIZT4JMX3IQGQ"
    ): Single<BalanceResponse>
}