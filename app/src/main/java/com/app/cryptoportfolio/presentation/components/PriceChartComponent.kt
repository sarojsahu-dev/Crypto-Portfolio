package com.app.cryptoportfolio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.cryptoportfolio.domain.model.ChartDataPoint
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import com.app.cryptoportfolio.ui.theme.TextSecondary
import com.app.cryptoportfolio.ui.theme.TextTertiary

@Composable
fun PriceChartComponent(
    dataPoints: List<ChartDataPoint>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BackgroundDark)
            .padding(16.dp),
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
                text = "Price Chart",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Text(
                text = "${dataPoints.size} data points",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
    }
}