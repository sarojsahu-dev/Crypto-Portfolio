package com.app.cryptoportfolio.domain.model

data class PriceChart(
    val cryptocurrency: Cryptocurrency,
    val timeframe: ChartTimeframe,
    val dataPoints: List<ChartDataPoint>
)

enum class ChartTimeframe {
    HOUR_1, HOUR_8, DAY_1, WEEK_1, MONTH_1, MONTH_6, YEAR_1
}
