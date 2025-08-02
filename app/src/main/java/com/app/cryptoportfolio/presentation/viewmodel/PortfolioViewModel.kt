package com.app.cryptoportfolio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cryptoportfolio.domain.usecase.FormatCurrencyUseCase
import com.app.cryptoportfolio.domain.usecase.GetPortfolioUseCase
import com.app.cryptoportfolio.domain.usecase.GetTransactionsUseCase
import com.app.cryptoportfolio.presentation.state.PortfolioUiState
import com.app.cryptoportfolio.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val formatCurrencyUseCase: FormatCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()

    init {
        loadPortfolioData()
        loadTransactions()
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

    private fun loadTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase()
                .catch { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            transactions = UiState.Error(exception.message ?: "Unknown error")
                        )
                    }
                }
                .collect { transactions ->
                    val recentTransactions = transactions.take(5)
                    _uiState.update { currentState ->
                        currentState.copy(
                            transactions = UiState.Success(recentTransactions)
                        )
                    }
                }
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadPortfolioData()
        loadTransactions()
    }

    fun formatCurrency(amount: java.math.BigDecimal, currency: String): String {
        return formatCurrencyUseCase(amount, currency)
    }

    fun formatPercentage(percentage: Double): String {
        val sign = if (percentage >= 0) "+" else ""
        return "$sign${String.format("%.2f", percentage)}%"
    }
}