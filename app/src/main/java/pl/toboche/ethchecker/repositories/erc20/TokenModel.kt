package pl.toboche.ethchecker.repositories.erc20

import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("address")
    val address: String,
    @SerializedName("symbol")
    val symbol: String
)
