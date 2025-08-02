package com.app.cryptoportfolio.data.source

import com.app.cryptoportfolio.domain.model.ChartDataPoint
import com.app.cryptoportfolio.domain.model.ChartTimeframe
import com.app.cryptoportfolio.domain.model.CryptoHolding
import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.ExchangeRate
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.domain.model.TransactionStatus
import com.app.cryptoportfolio.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockDataSource @Inject constructor() {
    val cryptocurrencies = listOf(
        Cryptocurrency(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            currentPrice = BigDecimal("76625024"),
            priceChangePercentage24h = 3.2,
            icon = "btc"
        ),
        Cryptocurrency(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            currentPrice = BigDecimal("179102.50"),
            priceChangePercentage24h = 2.5,
            icon = "eth"
        ),
        Cryptocurrency(
            id = "litecoin",
            symbol = "LTC",
            name = "Litecoin",
            currentPrice = BigDecimal("8500.00"),
            priceChangePercentage24h = -1.8,
            icon = "ltc"
        ),
        Cryptocurrency(
            id = "inr",
            symbol = "INR",
            name = "Indian Rupee",
            currentPrice = BigDecimal("1.0"),
            priceChangePercentage24h = 0.0,
            icon = "inr"
        )
    )

    val portfolio = Portfolio(
        totalValue = BigDecimal("157342.05"),
        totalChangePercentage = 4.6,
        holdings = listOf(
            CryptoHolding(
                cryptocurrency = cryptocurrencies[0],
                amount = BigDecimal("0.015"),
                currentValue = BigDecimal("114937.54"),
                changePercentage = 3.2
            ),
            CryptoHolding(
                cryptocurrency = cryptocurrencies[1],
                amount = BigDecimal("2.640"),
                currentValue = BigDecimal("472830.60"),
                changePercentage = 2.5
            ),
            CryptoHolding(
                cryptocurrency = cryptocurrencies[3],
                amount = BigDecimal("157342.05"),
                currentValue = BigDecimal("157342.05"),
                changePercentage = 4.6
            )
        )
    )

    val transactions = listOf(
        Transaction(
            id = "txn_001",
            type = TransactionType.RECEIVE,
            cryptocurrency = cryptocurrencies[0],
            amount = BigDecimal("0.002126"),
            price = BigDecimal("76625024"),
            timestamp = LocalDateTime.now().minusDays(1),
            status = TransactionStatus.COMPLETED
        ),
        Transaction(
            id = "txn_002",
            type = TransactionType.SEND,
            cryptocurrency = cryptocurrencies[1],
            amount = BigDecimal("0.003126"),
            price = BigDecimal("179102.50"),
            timestamp = LocalDateTime.now().minusDays(2),
            status = TransactionStatus.COMPLETED
        ),
        Transaction(
            id = "txn_003",
            type = TransactionType.SEND,
            cryptocurrency = cryptocurrencies[2],
            amount = BigDecimal("0.02126"),
            price = BigDecimal("8500.00"),
            timestamp = LocalDateTime.now().minusDays(3),
            status = TransactionStatus.COMPLETED
        ),
        Transaction(
            id = "txn_004",
            type = TransactionType.RECEIVE,
            cryptocurrency = cryptocurrencies[0],
            amount = BigDecimal("0.001500"),
            price = BigDecimal("74000.00"),
            timestamp = LocalDateTime.now().minusDays(5),
            status = TransactionStatus.COMPLETED
        )
    )

    val exchangeRates = mapOf(
        "ETH_INR" to ExchangeRate(
            fromCurrency = "ETH",
            toCurrency = "INR",
            rate = BigDecimal("176138.80"),
            spread = 0.2,
            gasFee = BigDecimal("422.73")
        ),
        "INR_ETH" to ExchangeRate(
            fromCurrency = "INR",
            toCurrency = "ETH",
            rate = BigDecimal("0.00000568"),
            spread = 0.2,
            gasFee = BigDecimal("422.73")
        ),
        "BTC_INR" to ExchangeRate(
            fromCurrency = "BTC",
            toCurrency = "INR",
            rate = BigDecimal("76500000"),
            spread = 0.15,
            gasFee = BigDecimal("800.00")
        )
    )

    fun generateChartData(cryptoId: String, timeframe: ChartTimeframe): List<ChartDataPoint> {
        val basePrice = when(cryptoId) {
            "bitcoin" -> BigDecimal("76625024")
            "ethereum" -> BigDecimal("179102.50")
            "litecoin" -> BigDecimal("8500.00")
            else -> BigDecimal("1.0")
        }

        val dataPoints = mutableListOf<ChartDataPoint>()
        val pointCount = when(timeframe) {
            ChartTimeframe.HOUR_1 -> 60
            ChartTimeframe.HOUR_8 -> 48
            ChartTimeframe.DAY_1 -> 24
            ChartTimeframe.WEEK_1 -> 7
            ChartTimeframe.MONTH_1 -> 30
            ChartTimeframe.MONTH_6 -> 180
            ChartTimeframe.YEAR_1 -> 365
        }

        repeat(pointCount) { index ->
            val timestamp = when(timeframe) {
                ChartTimeframe.HOUR_1 -> LocalDateTime.now().minusMinutes(index.toLong())
                ChartTimeframe.HOUR_8 -> LocalDateTime.now().minusMinutes(index * 10L)
                ChartTimeframe.DAY_1 -> LocalDateTime.now().minusHours(index.toLong())
                ChartTimeframe.WEEK_1 -> LocalDateTime.now().minusDays(index.toLong())
                ChartTimeframe.MONTH_1 -> LocalDateTime.now().minusDays(index.toLong())
                ChartTimeframe.MONTH_6 -> LocalDateTime.now().minusDays(index.toLong())
                ChartTimeframe.YEAR_1 -> LocalDateTime.now().minusDays(index.toLong())
            }

            val variation = (Math.random() - 0.5) * 0.05
            val price = basePrice.multiply(BigDecimal(1.0 + variation))

            dataPoints.add(
                ChartDataPoint(
                    timestamp = timestamp,
                    value = price
                )
            )
        }

        return dataPoints.reversed()
    }
}