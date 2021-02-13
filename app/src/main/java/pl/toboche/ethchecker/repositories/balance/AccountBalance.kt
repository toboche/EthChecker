package pl.toboche.ethchecker.repositories.balance

import java.math.BigDecimal

data class AccountBalance(
    val ethereum: BigDecimal,
    val dollars: BigDecimal
)