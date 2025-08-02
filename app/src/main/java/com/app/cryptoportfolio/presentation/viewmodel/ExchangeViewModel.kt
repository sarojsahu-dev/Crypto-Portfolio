package com.app.cryptoportfolio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cryptoportfolio.domain.usecase.ExecuteExchangeUseCase
import com.app.cryptoportfolio.domain.usecase.FormatCurrencyUseCase
import com.app.cryptoportfolio.domain.usecase.GetExchangeRateUseCase
import com.app.cryptoportfolio.domain.usecase.GetPortfolioUseCase
import com.app.cryptoportfolio.presentation.state.ExchangeUiState
import com.app.cryptoportfolio.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val executeExchangeUseCase: ExecuteExchangeUseCase,
    private val formatCurrencyUseCase: FormatCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeUiState())
    val uiState: StateFlow<ExchangeUiState> = _uiState.asStateFlow()

    init {
        loadPortfolioBalances()
        loadExchangeRate()
    }

    private fun loadPortfolioBalances() {
        viewModelScope.launch {
            getPortfolioUseCase()
                .collect { portfolio ->
                    val balances = portfolio.holdings.associate { holding ->
                        holding.cryptocurrency.symbol to holding.amount
                    }
                    _uiState.update { currentState ->
                        currentState.copy(availableBalances = balances)
                    }
                }
        }
    }

    private fun loadExchangeRate() {
        viewModelScope.launch {
            val currentState = _uiState.value
            getExchangeRateUseCase(currentState.fromCurrency, currentState.toCurrency)
                .catch { exception ->
                    _uiState.update { state ->
                        state.copy(
                            exchangeRate = UiState.Error(exception.message ?: "Unknown error")
                        )
                    }
                }
                .collect { rate ->
                    _uiState.update { state ->
                        state.copy(exchangeRate = UiState.Success(rate))
                    }
                    calculateToAmount()
                }
        }
    }

    fun updateFromAmount(amount: String) {
        _uiState.update { currentState ->
            currentState.copy(fromAmount = amount)
        }
        calculateToAmount()
    }

    fun swapCurrencies() {
        _uiState.update { currentState ->
            currentState.copy(
                fromCurrency = currentState.toCurrency,
                toCurrency = currentState.fromCurrency,
                fromAmount = currentState.toAmount,
                toAmount = "",
                exchangeRate = UiState.Loading
            )
        }
        loadExchangeRate()
    }

    fun selectFromCurrency(currency: String) {
        if (currency != _uiState.value.fromCurrency) {
            _uiState.update { currentState ->
                currentState.copy(
                    fromCurrency = currency,
                    exchangeRate = UiState.Loading
                )
            }
            loadExchangeRate()
        }
    }

    fun selectToCurrency(currency: String) {
        if (currency != _uiState.value.toCurrency) {
            _uiState.update { currentState ->
                currentState.copy(
                    toCurrency = currency,
                    exchangeRate = UiState.Loading
                )
            }
            loadExchangeRate()
        }
    }

    private fun calculateToAmount() {
        val currentState = _uiState.value
        val exchangeRateData = (currentState.exchangeRate as? UiState.Success)?.data

        if (exchangeRateData != null && currentState.fromAmount.isNotBlank()) {
            try {
                val fromAmount = BigDecimal(currentState.fromAmount)
                val toAmount = fromAmount.multiply(exchangeRateData.rate)
                    .setScale(6, RoundingMode.HALF_UP)

                _uiState.update { state ->
                    state.copy(toAmount = toAmount.toString())
                }
            } catch (e: NumberFormatException) {
                _uiState.update { state ->
                    state.copy(toAmount = "")
                }
            }
        }
    }

    fun executeExchange() {
        val currentState = _uiState.value
        val exchangeRateData = (currentState.exchangeRate as? UiState.Success)?.data

        if (exchangeRateData != null && currentState.fromAmount.isNotBlank()) {
            _uiState.update { it.copy(isExchanging = true) }

            viewModelScope.launch {
                try {
                    val fromAmount = BigDecimal(currentState.fromAmount)
                    val result = executeExchangeUseCase(
                        fromCurrency = currentState.fromCurrency,
                        toCurrency = currentState.toCurrency,
                        amount = fromAmount,
                        rate = exchangeRateData
                    )

                    _uiState.update { state ->
                        state.copy(
                            isExchanging = false,
                            exchangeResult = if (result.isSuccess) {
                                UiState.Success(result.getOrThrow())
                            } else {
                                UiState.Error(result.exceptionOrNull()?.message ?: "Exchange failed")
                            }
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update { state ->
                        state.copy(
                            isExchanging = false,
                            exchangeResult = UiState.Error(e.message ?: "Exchange failed")
                        )
                    }
                }
            }
        }
    }

    fun clearExchangeResult() {
        _uiState.update { it.copy(exchangeResult = null) }
    }

    fun formatCurrency(amount: BigDecimal, currency: String): String {
        return formatCurrencyUseCase(amount, currency)
    }

    fun isValidAmount(amount: String): Boolean {
        return try {
            val amountDecimal = BigDecimal(amount)
            amountDecimal > BigDecimal.ZERO
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun hasInsufficientBalance(): Boolean {
        val currentState = _uiState.value
        val balance = currentState.availableBalances[currentState.fromCurrency] ?: BigDecimal.ZERO
        return try {
            val requestedAmount = BigDecimal(currentState.fromAmount)
            requestedAmount > balance
        } catch (e: NumberFormatException) {
            false
        }
    }
}