package pl.toboche.ethchecker.repositories.price

import com.google.gson.annotations.SerializedName

data class PriceResponse(
    @SerializedName("ethereum")
    val ethereum: CryptoPrice
)