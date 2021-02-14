package pl.toboche.ethchecker.base

import javax.inject.Inject
import javax.inject.Singleton

//this object could also be injected if needed
@Singleton
open class ConfigurationConstants @Inject constructor() {
    val ethereumAddress = "0xde57844f758a0a6a1910a4787ab2f7121c8978c3"
    val etherscanApiUrl = "https://api.etherscan.io/"
    val coingeckoApiUrl = "https://api.coingecko.com/"
}