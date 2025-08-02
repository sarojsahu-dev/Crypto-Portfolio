package com.app.cryptoportfolio.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.presentation.components.ErrorMessage
import com.app.cryptoportfolio.presentation.components.LoadingIndicator
import com.app.cryptoportfolio.presentation.components.TransactionItem
import com.app.cryptoportfolio.presentation.state.TransactionFilter
import com.app.cryptoportfolio.presentation.state.UiState
import com.app.cryptoportfolio.presentation.viewmodel.RecordViewModel
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import com.app.cryptoportfolio.ui.theme.PrimaryBlue
import com.app.cryptoportfolio.ui.theme.SurfaceCard
import com.app.cryptoportfolio.ui.theme.TextPrimary
import com.app.cryptoportfolio.ui.theme.TextSecondary
import com.app.cryptoportfolio.ui.theme.TextTertiary

@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        Text(
            text = "Transactions",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(TransactionFilter.values()) { filter ->
                FilterChip(
                    onClick = { viewModel.selectFilter(filter) },
                    label = {
                        Text(
                            text = viewModel.getFilterDisplayName(filter),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    selected = uiState.selectedFilter == filter,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (uiState.selectedFilter == filter) PrimaryBlue else SurfaceCard,
                        labelColor = if (uiState.selectedFilter == filter) TextPrimary else TextSecondary,
                        selectedContainerColor = PrimaryBlue,
                        selectedLabelColor = TextPrimary
                    )
                )
            }
        }

        when (val transactionsState = uiState.transactions) {
            is UiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                ErrorMessage(
                    message = transactionsState.message,
                    onRetry = { viewModel.refresh() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                TransactionsContent(
                    transactions = uiState.filteredTransactions,
                    formatCurrency = viewModel::formatCurrency,
                    formatDate = viewModel::formatDate,
                    getTransactionDisplayType = viewModel::getTransactionDisplayType
                )
            }
        }
    }
}

@Composable
private fun TransactionsContent(
    transactions: List<Transaction>,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    formatDate: (Transaction) -> String,
    getTransactionDisplayType: (Transaction) -> String
) {
    if (transactions.isEmpty()) {
        EmptyTransactionsState()
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Last 4 days",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(transactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    formatCurrency = formatCurrency
                )
            }
        }
    }
}

@Composable
private fun EmptyTransactionsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "📝",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "No transactions found",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary
            )
            Text(
                text = "Your transaction history will appear here",
                style = MaterialTheme.typography.bodyMedium,
                color = TextTertiary
            )
        }
    }
}