package pl.toboche.ethchecker.repositories.erc20

import com.google.gson.annotations.SerializedName

data class TokensResponse(
    @SerializedName("tokens")
    val tokenModels: List<TokenModel>
)
