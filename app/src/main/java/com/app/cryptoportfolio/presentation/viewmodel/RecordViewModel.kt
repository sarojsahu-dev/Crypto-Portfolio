package com.app.cryptoportfolio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.domain.model.TransactionType
import com.app.cryptoportfolio.domain.usecase.FormatCurrencyUseCase
import com.app.cryptoportfolio.domain.usecase.GetTransactionsUseCase
import com.app.cryptoportfolio.presentation.state.RecordUiState
import com.app.cryptoportfolio.presentation.state.TransactionFilter
import com.app.cryptoportfolio.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val formatCurrencyUseCase: FormatCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase()
                .catch { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            transactions = UiState.Error(exception.message ?: "Unknown error"),
                            isRefreshing = false
                        )
                    }
                }
                .collect { transactions ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            transactions = UiState.Success(transactions),
                            isRefreshing = false
                        )
                    }
                    applyFilter(_uiState.value.selectedFilter)
                }
        }
    }

    fun selectFilter(filter: TransactionFilter) {
        _uiState.update { currentState ->
            currentState.copy(selectedFilter = filter)
        }
        applyFilter(filter)
    }

    private fun applyFilter(filter: TransactionFilter) {
        val currentState = _uiState.value
        val allTransactions = (currentState.transactions as? UiState.Success)?.data ?: return

        val filteredTransactions = when (filter) {
            TransactionFilter.ALL -> allTransactions
            TransactionFilter.SENT -> allTransactions.filter {
                it.type == TransactionType.SEND || it.type == TransactionType.SELL
            }
            TransactionFilter.RECEIVED -> allTransactions.filter {
                it.type == TransactionType.RECEIVE || it.type == TransactionType.BUY
            }
            TransactionFilter.BUY -> allTransactions.filter { it.type == TransactionType.BUY }
            TransactionFilter.SELL -> allTransactions.filter { it.type == TransactionType.SELL }
        }

        _uiState.update { state ->
            state.copy(filteredTransactions = filteredTransactions)
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadTransactions()
    }

    fun formatCurrency(amount: java.math.BigDecimal, currency: String): String {
        return formatCurrencyUseCase(amount, currency)
    }

    fun formatDate(transaction: Transaction): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        return transaction.timestamp.format(formatter)
    }

    fun getTransactionDisplayType(transaction: Transaction): String {
        return when (transaction.type) {
            TransactionType.SEND -> "Sent"
            TransactionType.RECEIVE -> "Received"
            TransactionType.BUY -> "Buy"
            TransactionType.SELL -> "Sell"
        }
    }

    fun getFilterDisplayName(filter: TransactionFilter): String {
        return when (filter) {
            TransactionFilter.ALL -> "All"
            TransactionFilter.SENT -> "Sent"
            TransactionFilter.RECEIVED -> "Received"
            TransactionFilter.BUY -> "Buy"
            TransactionFilter.SELL -> "Sell"
        }
    }

    fun getTransactionIcon(transaction: Transaction): String {
        return when (transaction.type) {
            TransactionType.SEND -> "↑"
            TransactionType.RECEIVE -> "↓"
            TransactionType.BUY -> "+"
            TransactionType.SELL -> "-"
        }
    }
}