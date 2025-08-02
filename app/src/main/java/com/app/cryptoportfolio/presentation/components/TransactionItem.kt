package com.app.cryptoportfolio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.domain.model.TransactionType
import com.app.cryptoportfolio.ui.theme.GreenSuccess
import com.app.cryptoportfolio.ui.theme.PrimaryBlue
import com.app.cryptoportfolio.ui.theme.RedError
import com.app.cryptoportfolio.ui.theme.SurfaceCard
import com.app.cryptoportfolio.ui.theme.TextPrimary
import com.app.cryptoportfolio.ui.theme.TextSecondary
import com.app.cryptoportfolio.ui.theme.YellowWarning
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transaction: Transaction,
    formatCurrency: (java.math.BigDecimal, String) -> String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            when (transaction.type) {
                                TransactionType.SEND -> RedError.copy(alpha = 0.2f)
                                TransactionType.RECEIVE -> GreenSuccess.copy(alpha = 0.2f)
                                TransactionType.BUY -> PrimaryBlue.copy(alpha = 0.2f)
                                TransactionType.SELL -> YellowWarning.copy(alpha = 0.2f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (transaction.type) {
                            TransactionType.SEND -> "↑"
                            TransactionType.RECEIVE -> "↓"
                            TransactionType.BUY -> "+"
                            TransactionType.SELL -> "-"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = when (transaction.type) {
                            TransactionType.SEND -> RedError
                            TransactionType.RECEIVE -> GreenSuccess
                            TransactionType.BUY -> PrimaryBlue
                            TransactionType.SELL -> YellowWarning
                        },
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = when (transaction.type) {
                            TransactionType.SEND -> "Sent"
                            TransactionType.RECEIVE -> "Received"
                            TransactionType.BUY -> "Buy"
                            TransactionType.SELL -> "Sell"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = transaction.timestamp.format(DateTimeFormatter.ofPattern("dd MMM")),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = transaction.cryptocurrency.symbol,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${if (transaction.type == TransactionType.SEND || transaction.type == TransactionType.SELL) "-" else "+"}${
                        formatCurrency(
                            transaction.amount,
                            transaction.cryptocurrency.symbol
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (transaction.type == TransactionType.SEND || transaction.type == TransactionType.SELL) RedError else GreenSuccess
                )
            }
        }
    }
}