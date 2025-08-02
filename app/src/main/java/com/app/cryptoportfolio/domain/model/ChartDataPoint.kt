package com.app.cryptoportfolio.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class ChartDataPoint(
    val timestamp: LocalDateTime,
    val value: BigDecimal
)
