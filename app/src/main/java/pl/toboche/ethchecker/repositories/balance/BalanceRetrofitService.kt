package pl.toboche.ethchecker.repositories.balance

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BalanceRetrofitService {

    companion object {
        //One might want to inject this value into the using service, depending on the future usage
        const val API_TOKEN = "28VA2X5T639Q4V9I5J2TWMIZT4JMX3IQGQ"
    }

    @GET("api?module=account&action=balance&tag=latest&apiKey=$API_TOKEN")
    fun getBalance(
        @Query("address")
        address: String,
    ): Single<BalanceResponse>

    @GET("api?module=account&action=tokenbalance&tag=latest&apiKey=$API_TOKEN")
    //One might want to inject these into the using service, depending on the future usage
    fun getErc20TokenBalance(
        @Query("address")
        address: String,
        @Query("contractaddress")
        contractAddress: String? = null
    ): Single<BalanceResponse>
}