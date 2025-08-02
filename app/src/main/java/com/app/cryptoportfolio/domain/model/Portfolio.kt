package com.app.cryptoportfolio.domain.model

import java.math.BigDecimal

data class Portfolio(
    val totalValue: BigDecimal,
    val totalChangePercentage: Double,
    val holdings: List<CryptoHolding>
)


data class CryptoHolding(
    val cryptocurrency: Cryptocurrency,
    val amount: BigDecimal,
    val currentValue: BigDecimal,
    val changePercentage: Double
)