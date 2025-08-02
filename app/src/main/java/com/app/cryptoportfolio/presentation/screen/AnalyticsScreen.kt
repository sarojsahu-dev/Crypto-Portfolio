package com.app.cryptoportfolio.presentation.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.app.cryptoportfolio.domain.model.ChartTimeframe
import com.app.cryptoportfolio.domain.model.CryptoHolding
import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.PriceChart
import com.app.cryptoportfolio.presentation.components.ErrorMessage
import com.app.cryptoportfolio.presentation.components.LoadingIndicator
import com.app.cryptoportfolio.presentation.components.PriceChartComponent
import com.app.cryptoportfolio.presentation.state.UiState
import com.app.cryptoportfolio.presentation.viewmodel.AnalyticsViewModel
import com.app.cryptoportfolio.ui.theme.Accent
import com.app.cryptoportfolio.ui.theme.BackgroundCard
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import com.app.cryptoportfolio.ui.theme.BitcoinOrange
import com.app.cryptoportfolio.ui.theme.EthereumBlue
import com.app.cryptoportfolio.ui.theme.GreenSuccess
import com.app.cryptoportfolio.ui.theme.LitecoinGray
import com.app.cryptoportfolio.ui.theme.PrimaryBlue
import com.app.cryptoportfolio.ui.theme.RedError
import com.app.cryptoportfolio.ui.theme.SurfaceCard
import com.app.cryptoportfolio.ui.theme.SurfaceVariant
import com.app.cryptoportfolio.ui.theme.TextPrimary
import com.app.cryptoportfolio.ui.theme.TextSecondary

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        when (val portfolioState = uiState.portfolio) {
            is UiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                ErrorMessage(
                    message = portfolioState.message,
                    onRetry = { viewModel.refresh() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                AnalyticsContent(
                    portfolio = portfolioState.data,
                    priceChart = uiState.priceChart,
                    selectedTimeframe = uiState.selectedTimeframe,
                    selectedCrypto = uiState.selectedCrypto,
                    onTimeframeSelected = viewModel::selectTimeframe,
                    onCryptoSelected = viewModel::selectCrypto,
                    formatCurrency = viewModel::formatCurrency,
                    getTimeframeDisplayName = viewModel::getTimeframeDisplayName
                )
            }
        }
    }
}

@Composable
private fun AnalyticsContent(
    portfolio: Portfolio,
    priceChart: UiState<PriceChart>,
    selectedTimeframe: ChartTimeframe,
    selectedCrypto: String,
    onTimeframeSelected: (ChartTimeframe) -> Unit,
    onCryptoSelected: (String) -> Unit,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    getTimeframeDisplayName: (ChartTimeframe) -> String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PortfolioOverviewCard(
                portfolio = portfolio,
                formatCurrency = formatCurrency
            )
        }

        item {
            Text(
                text = "Select Cryptocurrency",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(portfolio.holdings) { holding ->
                    CryptoSelectionChip(
                        crypto = holding.cryptocurrency,
                        isSelected = selectedCrypto == holding.cryptocurrency.id,
                        onSelected = { onCryptoSelected(holding.cryptocurrency.id) }
                    )
                }
            }
        }

        item {
            Text(
                text = "Price Chart",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            when (priceChart) {
                is UiState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
                is UiState.Error -> {
                    ErrorMessage(
                        message = priceChart.message,
                        onRetry = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
                is UiState.Success -> {
                    PriceChartCard(
                        priceChart = priceChart.data,
                        selectedTimeframe = selectedTimeframe,
                        onTimeframeSelected = onTimeframeSelected,
                        formatCurrency = formatCurrency,
                        getTimeframeDisplayName = getTimeframeDisplayName
                    )
                }
            }
        }

        item {
            Text(
                text = "Holdings Breakdown",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        items(portfolio.holdings) { holding ->
            HoldingBreakdownItem(
                holding = holding,
                totalPortfolioValue = portfolio.totalValue,
                formatCurrency = formatCurrency
            )
        }
    }
}

@Composable
private fun PortfolioOverviewCard(
    portfolio: Portfolio,
    formatCurrency: (java.math.BigDecimal, String) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundCard
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PrimaryBlue, Accent)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Total Portfolio Value",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(portfolio.totalValue, "INR"),
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${if (portfolio.totalChangePercentage >= 0) "+" else ""}${String.format("%.2f", portfolio.totalChangePercentage)}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (portfolio.totalChangePercentage >= 0) GreenSuccess else RedError,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CryptoSelectionChip(
    crypto: Cryptocurrency,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    FilterChip(
        onClick = onSelected,
        label = {
            Text(
                text = crypto.symbol,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        },
        selected = isSelected,
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when (crypto.symbol) {
                            "BTC" -> BitcoinOrange
                            "ETH" -> EthereumBlue
                            "LTC" -> LitecoinGray
                            else -> PrimaryBlue
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (crypto.symbol) {
                        "BTC" -> "₿"
                        "ETH" -> "Ξ"
                        "LTC" -> "Ł"
                        else -> crypto.symbol.take(1)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = if (isSelected) PrimaryBlue else SurfaceCard,
            labelColor = if (isSelected) TextPrimary else TextSecondary,
            selectedContainerColor = PrimaryBlue,
            selectedLabelColor = TextPrimary
        )
    )
}

@Composable
private fun PriceChartCard(
    priceChart: PriceChart,
    selectedTimeframe: ChartTimeframe,
    onTimeframeSelected: (ChartTimeframe) -> Unit,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    getTimeframeDisplayName: (ChartTimeframe) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = priceChart.cryptocurrency.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatCurrency(priceChart.cryptocurrency.currentPrice, "INR"),
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${if (priceChart.cryptocurrency.priceChangePercentage24h >= 0) "+" else ""}${String.format("%.2f", priceChart.cryptocurrency.priceChangePercentage24h)}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (priceChart.cryptocurrency.priceChangePercentage24h >= 0) GreenSuccess else RedError,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PriceChartComponent(
                dataPoints = priceChart.dataPoints,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Timeframe Selection
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ChartTimeframe.values()) { timeframe ->
                    FilterChip(
                        onClick = { onTimeframeSelected(timeframe) },
                        label = {
                            Text(
                                text = getTimeframeDisplayName(timeframe),
                                style = MaterialTheme.typography.bodySmall
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
    }
}

@Composable
private fun HoldingBreakdownItem(
    holding: CryptoHolding,
    totalPortfolioValue: java.math.BigDecimal,
    formatCurrency: (java.math.BigDecimal, String) -> String
) {
    val percentage = if (totalPortfolioValue > java.math.BigDecimal.ZERO) {
        holding.currentValue.divide(totalPortfolioValue, 4, java.math.RoundingMode.HALF_UP)
            .multiply(java.math.BigDecimal("100"))
    } else {
        java.math.BigDecimal.ZERO
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(16.dp))
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
                            style = MaterialTheme.typography.bodyMedium,
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
                            text = "${String.format("%.1f", percentage.toDouble())}% of portfolio",
                            style = MaterialTheme.typography.bodySmall,
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
                        text = formatCurrency(holding.amount, holding.cryptocurrency.symbol),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = (percentage.toDouble() / 100.0).toFloat().coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = when (holding.cryptocurrency.symbol) {
                    "BTC" -> BitcoinOrange
                    "ETH" -> EthereumBlue
                    "LTC" -> LitecoinGray
                    else -> PrimaryBlue
                },
                trackColor = SurfaceVariant
            )
        }
    }
}