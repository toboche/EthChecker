package pl.toboche.ethchecker.repositories.price

import com.google.gson.annotations.SerializedName

data class CryptoPrice(
    @SerializedName("usd")
    val usd: Double
)
