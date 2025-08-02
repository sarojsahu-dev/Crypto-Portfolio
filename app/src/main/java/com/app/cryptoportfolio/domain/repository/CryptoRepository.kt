package com.app.cryptoportfolio.domain.repository

import com.app.cryptoportfolio.domain.model.ChartTimeframe
import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.ExchangeRate
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.PriceChart
import com.app.cryptoportfolio.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getPortfolio(): Flow<Portfolio>
    fun getCryptocurrencies(): Flow<List<Cryptocurrency>>
    fun getCryptocurrency(id: String): Flow<Cryptocurrency?>
    fun getTransactions(): Flow<List<Transaction>>
    fun getExchangeRate(fromCurrency: String, toCurrency: String): Flow<ExchangeRate>
    fun getPriceChart(cryptoId: String, timeframe: ChartTimeframe): Flow<PriceChart>
    suspend fun updatePortfolio(portfolio: Portfolio)
    suspend fun addTransaction(transaction: Transaction)
}