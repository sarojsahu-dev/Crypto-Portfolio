package com.app.cryptoportfolio.presentation.state

import com.app.cryptoportfolio.domain.model.ExchangeRate
import com.app.cryptoportfolio.domain.model.Transaction
import java.math.BigDecimal

data class ExchangeUiState(
    val fromCurrency: String = "ETH",
    val toCurrency: String = "INR",
    val fromAmount: String = "2.640",
    val toAmount: String = "",
    val exchangeRate: UiState<ExchangeRate> = UiState.Loading,
    val availableBalances: Map<String, BigDecimal> = emptyMap(),
    val isExchanging: Boolean = false,
    val exchangeResult: UiState<Transaction>? = null
)
