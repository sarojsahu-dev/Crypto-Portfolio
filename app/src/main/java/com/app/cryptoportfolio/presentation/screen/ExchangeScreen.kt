package com.app.cryptoportfolio.presentation.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.cryptoportfolio.domain.model.ExchangeRate
import com.app.cryptoportfolio.presentation.components.ErrorMessage
import com.app.cryptoportfolio.presentation.components.LoadingIndicator
import com.app.cryptoportfolio.presentation.state.UiState
import com.app.cryptoportfolio.presentation.viewmodel.ExchangeViewModel
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import com.app.cryptoportfolio.ui.theme.BitcoinOrange
import com.app.cryptoportfolio.ui.theme.DividerColor
import com.app.cryptoportfolio.ui.theme.EthereumBlue
import com.app.cryptoportfolio.ui.theme.LitecoinGray
import com.app.cryptoportfolio.ui.theme.PrimaryBlue
import com.app.cryptoportfolio.ui.theme.RedError
import com.app.cryptoportfolio.ui.theme.SurfaceCard
import com.app.cryptoportfolio.ui.theme.TextPrimary
import com.app.cryptoportfolio.ui.theme.TextSecondary
import com.app.cryptoportfolio.ui.theme.TextTertiary
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExchangeViewModel = hiltViewModel()
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
                    text = "Exchange",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BackgroundDark
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExchangeAmountCard(
                title = "Send",
                currency = uiState.fromCurrency,
                amount = uiState.fromAmount,
                balance = uiState.availableBalances[uiState.fromCurrency] ?: BigDecimal.ZERO,
                onAmountChange = viewModel::updateFromAmount,
                onCurrencyClick = { /* Show currency selector */ },
                formatCurrency = viewModel::formatCurrency,
                isEditable = true
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { viewModel.swapCurrencies() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(SurfaceCard)
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Swap",
                        tint = TextPrimary
                    )
                }
            }

            ExchangeAmountCard(
                title = "Receive",
                currency = uiState.toCurrency,
                amount = uiState.toAmount,
                balance = BigDecimal.ZERO,
                onAmountChange = { },
                onCurrencyClick = { /* Show currency selector */ },
                formatCurrency = viewModel::formatCurrency,
                isEditable = false
            )

            when (val rateState = uiState.exchangeRate) {
                is UiState.Loading -> {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                }
                is UiState.Error -> {
                    ErrorMessage(
                        message = rateState.message,
                        onRetry = { /* Retry loading rate */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is UiState.Success -> {
                    ExchangeRateCard(
                        exchangeRate = rateState.data,
                        fromCurrency = uiState.fromCurrency,
                        toCurrency = uiState.toCurrency,
                        toAmount = uiState.toAmount,
                        formatCurrency = viewModel::formatCurrency
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.executeExchange() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isExchanging &&
                        viewModel.isValidAmount(uiState.fromAmount) &&
                        !viewModel.hasInsufficientBalance(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    disabledContainerColor = PrimaryBlue.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isExchanging) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = TextPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Exchange",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (viewModel.hasInsufficientBalance()) {
                Text(
                    text = "Insufficient balance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RedError,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    uiState.exchangeResult?.let { result ->
        when (result) {
            is UiState.Success -> {
                LaunchedEffect(result) {
                    viewModel.clearExchangeResult()
                }
            }
            is UiState.Error -> {
                LaunchedEffect(result) {
                    viewModel.clearExchangeResult()
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun ExchangeAmountCard(
    title: String,
    currency: String,
    amount: String,
    balance: BigDecimal,
    onAmountChange: (String) -> Unit,
    onCurrencyClick: () -> Unit,
    formatCurrency: (BigDecimal, String) -> String,
    isEditable: Boolean
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
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onCurrencyClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when (currency) {
                                    "BTC" -> BitcoinOrange
                                    "ETH" -> EthereumBlue
                                    "LTC" -> LitecoinGray
                                    else -> PrimaryBlue
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (currency) {
                                "BTC" -> "₿"
                                "ETH" -> "Ξ"
                                "LTC" -> "Ł"
                                "INR" -> "₹"
                                else -> currency.take(1)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currency,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEditable) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    placeholder = {
                        Text(
                            text = "0.000",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextTertiary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = DividerColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )
            } else {
                Text(
                    text = amount.ifBlank { "0.000" },
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditable && balance > BigDecimal.ZERO) {
                Text(
                    text = "Balance: ${formatCurrency(balance, currency)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
private fun ExchangeRateCard(
    exchangeRate: ExchangeRate,
    fromCurrency: String,
    toCurrency: String,
    toAmount: String,
    formatCurrency: (BigDecimal, String) -> String
) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Rate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "1 $fromCurrency = ${formatCurrency(exchangeRate.rate, toCurrency)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Spread",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "${String.format("%.1f", exchangeRate.spread)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }

            exchangeRate.gasFee?.let { gasFee ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Gas fee",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = formatCurrency(gasFee, "INR"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }

            Divider(
                color = DividerColor,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "You will receive",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = if (toAmount.isNotBlank()) {
                        formatCurrency(BigDecimal(toAmount), toCurrency)
                    } else {
                        "0.000 $toCurrency"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}