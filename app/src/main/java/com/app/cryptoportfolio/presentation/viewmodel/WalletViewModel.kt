package com.app.cryptoportfolio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cryptoportfolio.domain.usecase.FormatCurrencyUseCase
import com.app.cryptoportfolio.domain.usecase.GetCryptocurrenciesUseCase
import com.app.cryptoportfolio.domain.usecase.GetPortfolioUseCase
import com.app.cryptoportfolio.presentation.state.UiState
import com.app.cryptoportfolio.presentation.state.WalletUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getCryptocurrenciesUseCase: GetCryptocurrenciesUseCase,
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val formatCurrencyUseCase: FormatCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadCryptocurrencies()
        loadPortfolio()
    }

    private fun loadCryptocurrencies() {
        viewModelScope.launch {
            getCryptocurrenciesUseCase()
                .catch { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            cryptocurrencies = UiState.Error(exception.message ?: "Unknown error"),
                            isRefreshing = false
                        )
                    }
                }
                .collect { cryptocurrencies ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            cryptocurrencies = UiState.Success(cryptocurrencies),
                            isRefreshing = false
                        )
                    }
                }
        }
    }

    private fun loadPortfolio() {
        viewModelScope.launch {
            getPortfolioUseCase()
                .catch { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            portfolio = UiState.Error(exception.message ?: "Unknown error")
                        )
                    }
                }
                .collect { portfolio ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            portfolio = UiState.Success(portfolio),
                            totalBalance = portfolio.totalValue
                        )
                    }
                }
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadCryptocurrencies()
        loadPortfolio()
    }

    fun formatCurrency(amount: BigDecimal, currency: String): String {
        return formatCurrencyUseCase(amount, currency)
    }

    fun formatPercentage(percentage: Double): String {
        val sign = if (percentage >= 0) "+" else ""
        return "$sign${String.format("%.2f", percentage)}%"
    }

    fun getCryptoIcon(symbol: String): String {
        return when (symbol.uppercase()) {
            "BTC" -> "₿"
            "ETH" -> "Ξ"
            "LTC" -> "Ł"
            "INR" -> "₹"
            else -> symbol.take(1)
        }
    }
}