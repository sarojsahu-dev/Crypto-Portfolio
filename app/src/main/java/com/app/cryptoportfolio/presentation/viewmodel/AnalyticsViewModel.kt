package com.app.cryptoportfolio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cryptoportfolio.domain.model.ChartTimeframe
import com.app.cryptoportfolio.domain.usecase.FormatCurrencyUseCase
import com.app.cryptoportfolio.domain.usecase.GetPortfolioUseCase
import com.app.cryptoportfolio.domain.usecase.GetPriceChartUseCase
import com.app.cryptoportfolio.presentation.state.AnalyticsUiState
import com.app.cryptoportfolio.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val getPriceChartUseCase: GetPriceChartUseCase,
    private val formatCurrencyUseCase: FormatCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadPortfolioData()
        loadPriceChart()
    }

    private fun loadPortfolioData() {
        viewModelScope.launch {
            getPortfolioUseCase()
                .catch { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            portfolio = UiState.Error(exception.message ?: "Unknown error"),
                            isRefreshing = false
                        )
                    }
                }
                .collect { portfolio ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            portfolio = UiState.Success(portfolio),
                            isRefreshing = false
                        )
                    }
                }
        }
    }

    private fun loadPriceChart() {
        viewModelScope.launch {
            val currentState = _uiState.value
            getPriceChartUseCase(currentState.selectedCrypto, currentState.selectedTimeframe)
                .catch { exception ->
                    _uiState.update { state ->
                        state.copy(
                            priceChart = UiState.Error(exception.message ?: "Unknown error")
                        )
                    }
                }
                .collect { chart ->
                    _uiState.update { state ->
                        state.copy(priceChart = UiState.Success(chart))
                    }
                }
        }
    }

    fun selectTimeframe(timeframe: ChartTimeframe) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedTimeframe = timeframe,
                priceChart = UiState.Loading
            )
        }
        loadPriceChart()
    }

    fun selectCrypto(cryptoId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCrypto = cryptoId,
                priceChart = UiState.Loading
            )
        }
        loadPriceChart()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadPortfolioData()
        loadPriceChart()
    }

    fun formatCurrency(amount: java.math.BigDecimal, currency: String): String {
        return formatCurrencyUseCase(amount, currency)
    }

    fun getTimeframeDisplayName(timeframe: ChartTimeframe): String {
        return when (timeframe) {
            ChartTimeframe.HOUR_1 -> "1h"
            ChartTimeframe.HOUR_8 -> "8h"
            ChartTimeframe.DAY_1 -> "1d"
            ChartTimeframe.WEEK_1 -> "1w"
            ChartTimeframe.MONTH_1 -> "1m"
            ChartTimeframe.MONTH_6 -> "6m"
            ChartTimeframe.YEAR_1 -> "1y"
        }
    }
}