package com.app.cryptoportfolio.presentation.state

import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.Transaction

data class PortfolioUiState(
    val portfolio: UiState<Portfolio> = UiState.Loading,
    val transactions: UiState<List<Transaction>> = UiState.Loading,
    val isRefreshing: Boolean = false
)
