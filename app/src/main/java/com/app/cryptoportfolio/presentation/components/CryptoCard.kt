package com.app.cryptoportfolio.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CryptoCard(
    cryptoName: String,
    symbol: String,
    amount: String,
    percentage: String,
    isPositive: Boolean,
    iconText: String,
    iconBgColor: Color,
    modifier: Modifier = Modifier
) {

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF151517),
            Color(0xFF2B2B2B),
            Color(0xFF151517)
        )
    )
    Card(
        modifier = modifier
            .width(204.dp)
            .height(118.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D0C0D)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderBrush)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconText,
                        color = if (iconBgColor == Color.White) Color.Black else Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = cryptoName,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = amount,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Text(
                    text = percentage,
                    color = if (isPositive) Color(0xFF00C851) else Color(0xFFFF4444),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun CryptoCardRowPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF000000))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CryptoCard(
            cryptoName = "Bitcoin (BTC)",
            symbol = "BTC",
            amount = "₹ 75,62,502.14",
            percentage = "+3.2%",
            isPositive = true,
            iconText = "₿",
            iconBgColor = Color(0xFFF7931A)
        )

        CryptoCard(
            cryptoName = "Ether (ETH)",
            symbol = "ETH",
            amount = "₹ 1,79,102.50",
            percentage = "+3.2%",
            isPositive = true,
            iconText = "⧫",
            iconBgColor = Color.White
        )
    }
}
