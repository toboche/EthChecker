package pl.toboche.ethchecker.mockserver

object APIPaths {

    const val ETHERSCAN_API_ETH_BALANCE =
        "/api?module=account&action=balance&tag=latest&apiKey=28VA2X5T639Q4V9I5J2TWMIZT4JMX3IQGQ&address=0xde57844f758a0a6a1910a4787ab2f7121c8978c3"

    const val COINGECKO_API_ETH_PRICE =
        "/api/v3/simple/price?ids=ethereum&vs_currencies=usd"
}
