package com.app.cryptoportfolio.domain.model

import java.math.BigDecimal

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: BigDecimal,
    val spread: Double,
    val gasFee: BigDecimal? = null
)
