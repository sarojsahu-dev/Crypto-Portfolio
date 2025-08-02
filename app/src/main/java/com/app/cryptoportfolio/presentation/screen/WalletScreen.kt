package com.app.cryptoportfolio.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
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
import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.presentation.components.ErrorMessage
import com.app.cryptoportfolio.presentation.components.LoadingIndicator
import com.app.cryptoportfolio.presentation.state.UiState
import com.app.cryptoportfolio.presentation.viewmodel.WalletViewModel
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
import com.app.cryptoportfolio.ui.theme.TextPrimary
import com.app.cryptoportfolio.ui.theme.TextSecondary

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    viewModel: WalletViewModel = hiltViewModel()
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
                WalletContent(
                    portfolio = portfolioState.data,
                    cryptocurrencies = uiState.cryptocurrencies,
                    formatCurrency = viewModel::formatCurrency,
                    formatPercentage = viewModel::formatPercentage,
                    getCryptoIcon = viewModel::getCryptoIcon
                )
            }
        }
    }
}

@Composable
private fun WalletContent(
    portfolio: Portfolio,
    cryptocurrencies: UiState<List<Cryptocurrency>>,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    formatPercentage: (Double) -> String,
    getCryptoIcon: (String) -> String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WalletBalanceCard(
                portfolio = portfolio,
                formatCurrency = formatCurrency,
                formatPercentage = formatPercentage
            )
        }

        item {
            QuickActionsRow()
        }

        item {
            Text(
                text = "Assets",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(portfolio.holdings) { holding ->
            WalletAssetItem(
                holding = holding,
                formatCurrency = formatCurrency,
                formatPercentage = formatPercentage,
                getCryptoIcon = getCryptoIcon
            )
        }

        when (cryptocurrencies) {
            is UiState.Loading -> {
                item {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }

            is UiState.Error -> {
                item {
                    ErrorMessage(
                        message = cryptocurrencies.message,
                        onRetry = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }

            is UiState.Success -> {
                item {
                    Text(
                        text = "All Cryptocurrencies",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                val holdingSymbols = portfolio.holdings.map { it.cryptocurrency.symbol }.toSet()
                val availableCryptos = cryptocurrencies.data.filter { it.symbol !in holdingSymbols }

                items(availableCryptos) { crypto ->
                    AvailableCryptoItem(
                        cryptocurrency = crypto,
                        formatCurrency = formatCurrency,
                        formatPercentage = formatPercentage,
                        getCryptoIcon = getCryptoIcon
                    )
                }
            }
        }
    }
}

@Composable
private fun WalletBalanceCard(
    portfolio: Portfolio,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    formatPercentage: (Double) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
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
                        colors = listOf(Accent, PrimaryBlue)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Total Balance",
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
                    text = formatPercentage(portfolio.totalChangePercentage),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (portfolio.totalChangePercentage >= 0) GreenSuccess else RedError,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun QuickActionsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .height(80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceCard
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .height(80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceCard
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "QR Code",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Receive",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun WalletAssetItem(
    holding: CryptoHolding,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    formatPercentage: (Double) -> String,
    getCryptoIcon: (String) -> String
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
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
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
                        text = getCryptoIcon(holding.cryptocurrency.symbol),
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = holding.cryptocurrency.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = holding.cryptocurrency.symbol,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatCurrency(holding.amount, holding.cryptocurrency.symbol),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Text(
                        text = formatPercentage(holding.changePercentage),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (holding.changePercentage >= 0) GreenSuccess else RedError,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AvailableCryptoItem(
    cryptocurrency: Cryptocurrency,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    formatPercentage: (Double) -> String,
    getCryptoIcon: (String) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard.copy(alpha = 0.6f)
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
                            when (cryptocurrency.symbol) {
                                "BTC" -> BitcoinOrange
                                "ETH" -> EthereumBlue
                                "LTC" -> LitecoinGray
                                else -> PrimaryBlue
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getCryptoIcon(cryptocurrency.symbol),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = cryptocurrency.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = cryptocurrency.symbol,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatCurrency(cryptocurrency.currentPrice, "INR"),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatPercentage(cryptocurrency.priceChangePercentage24h),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (cryptocurrency.priceChangePercentage24h >= 0) GreenSuccess else RedError,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}