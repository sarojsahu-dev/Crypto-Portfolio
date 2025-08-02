package com.app.cryptoportfolio.presentation.state

import com.app.cryptoportfolio.domain.model.Transaction

data class RecordUiState(
    val transactions: UiState<List<Transaction>> = UiState.Loading,
    val filteredTransactions: List<Transaction> = emptyList(),
    val selectedFilter: TransactionFilter = TransactionFilter.ALL,
    val isRefreshing: Boolean = false
)


enum class TransactionFilter {
    ALL, SENT, RECEIVED, BUY, SELL
}