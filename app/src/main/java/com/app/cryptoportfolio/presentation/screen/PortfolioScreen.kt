package com.app.cryptoportfolio.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.cryptoportfolio.domain.model.CryptoHolding
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.presentation.components.ErrorMessage
import com.app.cryptoportfolio.presentation.components.LoadingIndicator
import com.app.cryptoportfolio.presentation.components.TransactionItem
import com.app.cryptoportfolio.presentation.state.UiState
import com.app.cryptoportfolio.presentation.viewmodel.PortfolioViewModel
import com.app.cryptoportfolio.ui.theme.BackgroundCard
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import com.app.cryptoportfolio.ui.theme.BitcoinOrange
import com.app.cryptoportfolio.ui.theme.EthereumBlue
import com.app.cryptoportfolio.ui.theme.GradientEnd
import com.app.cryptoportfolio.ui.theme.GradientStart
import com.app.cryptoportfolio.ui.theme.GreenSuccess
import com.app.cryptoportfolio.ui.theme.LitecoinGray
import com.app.cryptoportfolio.ui.theme.PrimaryBlue
import com.app.cryptoportfolio.ui.theme.RedError
import com.app.cryptoportfolio.ui.theme.SurfaceCard
import com.app.cryptoportfolio.ui.theme.SurfaceVariant
import com.app.cryptoportfolio.ui.theme.TextPrimary
import com.app.cryptoportfolio.ui.theme.TextSecondary
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PortfolioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Portfolio Value",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = TextPrimary
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Handle notifications */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackgroundDark
            )
        )

        when (val portfolioState = uiState.portfolio) {
            is UiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {

            }
            is UiState.Success -> {
                PortfolioContent(
                    portfolio = portfolioState.data,
                    transactions = uiState.transactions,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { viewModel.refresh() },
                    formatCurrency = viewModel::formatCurrency,
                    formatPercentage = viewModel::formatPercentage
                )
            }
        }
    }
}

@Composable
private fun PortfolioContent(
    portfolio: Portfolio,
    transactions: UiState<List<Transaction>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    formatCurrency: (BigDecimal, String) -> String,
    formatPercentage: (Double) -> String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PortfolioValueCard(
                portfolio = portfolio,
                formatCurrency = formatCurrency,
                formatPercentage = formatPercentage
            )
        }

        item {
            TimeSelector(
                selectedTimeframe = "1d",
                onTimeframeSelected = { /* Handle timeframe selection */ }
            )
        }

        item {
            PortfolioTrendGraph(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        item {
            Text(
                text = "Holdings",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(portfolio.holdings) { holding ->
            HoldingItem(
                holding = holding,
                formatCurrency = formatCurrency,
                formatPercentage = formatPercentage
            )
        }

        item {
            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        when (transactions) {
            is UiState.Loading -> {
                item {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
            is UiState.Error -> {
                item {
                        ErrorMessage(
                            message = transactions.message,
                            onRetry = onRefresh,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                }
            }
            is UiState.Success -> {
                items(transactions.data) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        formatCurrency = formatCurrency
                    )
                }
            }
        }
    }
}

@Composable
private fun PortfolioValueCard(
    portfolio: Portfolio,
    formatCurrency: (BigDecimal, String) -> String,
    formatPercentage: (Double) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundCard
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(GradientStart, GradientEnd)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = "INR",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(portfolio.totalValue, "INR"),
                    style = MaterialTheme.typography.displayMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹1,342",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = formatPercentage(portfolio.totalChangePercentage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (portfolio.totalChangePercentage >= 0) GreenSuccess else RedError,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeSelector(
    selectedTimeframe: String,
    onTimeframeSelected: (String) -> Unit
) {
    val timeframes = listOf("1h", "8h", "1d", "1w", "1m", "6m", "1y")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(timeframes) { timeframe ->
            FilterChip(
                onClick = { onTimeframeSelected(timeframe) },
                label = {
                    Text(
                        text = timeframe,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (selectedTimeframe == timeframe) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                selected = selectedTimeframe == timeframe,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = if (selectedTimeframe == timeframe) PrimaryBlue else SurfaceVariant,
                    labelColor = if (selectedTimeframe == timeframe) TextPrimary else TextSecondary,
                    selectedContainerColor = PrimaryBlue,
                    selectedLabelColor = TextPrimary
                )
            )
        }
    }
}

@Composable
private fun PortfolioTrendGraph(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Portfolio Trend",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Mock graph visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BackgroundDark),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📈",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Portfolio Growth Chart",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "+4.6% Today",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreenSuccess,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun HoldingItem(
    holding: CryptoHolding,
    formatCurrency: (BigDecimal, String) -> String,
    formatPercentage: (Double) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Crypto Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            when (holding.cryptocurrency.symbol) {
                                "BTC" -> BitcoinOrange
                                "ETH" -> EthereumBlue
                                "LTC" -> LitecoinGray
                                else -> PrimaryBlue
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (holding.cryptocurrency.symbol) {
                            "BTC" -> "₿"
                            "ETH" -> "Ξ"
                            "LTC" -> "Ł"
                            else -> holding.cryptocurrency.symbol.take(1)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = holding.cryptocurrency.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatCurrency(holding.amount, holding.cryptocurrency.symbol),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatCurrency(holding.currentValue, "INR"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatPercentage(holding.changePercentage),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (holding.changePercentage >= 0) GreenSuccess else RedError
                )
            }
        }
    }
}