package com.app.cryptoportfolio.presentation.state

import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.Portfolio
import java.math.BigDecimal

data class WalletUiState(
    val cryptocurrencies: UiState<List<Cryptocurrency>> = UiState.Loading,
    val portfolio: UiState<Portfolio> = UiState.Loading,
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val isRefreshing: Boolean = false
)

