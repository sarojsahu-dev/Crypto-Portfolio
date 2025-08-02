package com.app.cryptoportfolio.data.repository

import com.app.cryptoportfolio.data.source.MockDataSource
import com.app.cryptoportfolio.domain.model.ChartTimeframe
import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.ExchangeRate
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.PriceChart
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.domain.repository.CryptoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoRepositoryImpl @Inject constructor(
    private val mockDataSource: MockDataSource
) : CryptoRepository {

    private var currentPortfolio = mockDataSource.portfolio
    private val currentTransactions = mockDataSource.transactions.toMutableList()
    override fun getPortfolio(): Flow<Portfolio> =
        flow {
            emit(currentPortfolio)
            while (true) {
                delay(3000)
                val updatedHoldings = currentPortfolio.holdings.map { holding ->
                    val priceChange = (Math.random() - 0.5) * 0.02 // ±1% change
                    val newPrice = holding.cryptocurrency.currentPrice.multiply(
                        java.math.BigDecimal(1.0 + priceChange)
                    )
                    val updatedCrypto = holding.cryptocurrency.copy(
                        currentPrice = newPrice,
                        priceChangePercentage24h = holding.cryptocurrency.priceChangePercentage24h + priceChange * 100
                    )
                    holding.copy(
                        cryptocurrency = updatedCrypto,
                        currentValue = holding.amount.multiply(newPrice),
                        changePercentage = holding.changePercentage + priceChange * 100
                    )
                }

                val totalValue = updatedHoldings.sumOf { it.currentValue }
                val avgChangePercentage = updatedHoldings.map { it.changePercentage }.average()

                currentPortfolio = currentPortfolio.copy(
                    totalValue = totalValue,
                    totalChangePercentage = avgChangePercentage,
                    holdings = updatedHoldings
                )
                emit(currentPortfolio)
            }
        }


    override fun getCryptocurrencies(): Flow<List<Cryptocurrency>> =
        flowOf(mockDataSource.cryptocurrencies)

    override fun getCryptocurrency(id: String): Flow<Cryptocurrency?> = flow {
        val crypto = mockDataSource.cryptocurrencies.find { it.id == id }
        emit(crypto)
    }


    override fun getTransactions(): Flow<List<Transaction>> =
        flowOf(currentTransactions.sortedByDescending { it.timestamp })


    override fun getExchangeRate(fromCurrency: String, toCurrency: String): Flow<ExchangeRate> =
        flow {
            delay(1000)
            val key = "${fromCurrency}_${toCurrency}"
            val rate = mockDataSource.exchangeRates[key] ?: ExchangeRate(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                rate = java.math.BigDecimal("1.0"),
                spread = 0.0
            )
            emit(rate)
        }

    override fun getPriceChart(cryptoId: String, timeframe: ChartTimeframe): Flow<PriceChart> =
        flow {
            delay(800)
            val crypto = mockDataSource.cryptocurrencies.find { it.id == cryptoId }
            if (crypto != null) {
                val dataPoints = mockDataSource.generateChartData(cryptoId, timeframe)
                emit(
                    PriceChart(
                        cryptocurrency = crypto,
                        timeframe = timeframe,
                        dataPoints = dataPoints
                    )
                )
            }
        }

    override suspend fun updatePortfolio(portfolio: Portfolio) {
        currentPortfolio = portfolio
    }

    override suspend fun addTransaction(transaction: Transaction) {
        currentTransactions.add(0, transaction)
    }

}