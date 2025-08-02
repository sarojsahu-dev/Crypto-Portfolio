package com.app.cryptoportfolio.domain.model

import java.math.BigDecimal

data class Cryptocurrency(
    val id: String,
    val symbol: String,
    val name: String,
    val currentPrice: BigDecimal,
    val priceChangePercentage24h: Double,
    val icon: String
)
