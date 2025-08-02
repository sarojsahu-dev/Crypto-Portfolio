package com.app.cryptoportfolio.presentation.state

import com.app.cryptoportfolio.domain.model.ChartTimeframe
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.PriceChart

data class AnalyticsUiState(
    val portfolio: UiState<Portfolio> = UiState.Loading,
    val priceChart: UiState<PriceChart> = UiState.Loading,
    val selectedTimeframe: ChartTimeframe = ChartTimeframe.DAY_1,
    val selectedCrypto: String = "bitcoin",
    val isRefreshing: Boolean = false
)
