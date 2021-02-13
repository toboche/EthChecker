package pl.toboche.ethchecker.repositories.balance

import com.google.gson.annotations.SerializedName

data class BalanceResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: String
)