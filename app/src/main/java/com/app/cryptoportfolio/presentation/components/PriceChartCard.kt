package com.app.cryptoportfolio.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PriceChartCard(
    priceChart: List<ChartPoint>,
    selectedTimeframe: TimeFrame,
    onTimeframeSelected: (TimeFrame) -> Unit,
    formatCurrency: (Double) -> String,
    getTimeframeDisplayName: (TimeFrame) -> String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF08080a)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listOf("1h", "8h", "1d", "1w", "1m", "6m", "1y")) { timeframe ->
                    TimeframeChip(
                        timeframe = timeframe,
                        isSelected = timeframe == "6m", // Mock selected state
                        onClick = { /* onTimeframeSelected */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(
                    text = "24 March",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "₹ 1,42,340",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                PriceChart(
                    modifier = Modifier.fillMaxSize()
                )

                // Chart point indicator
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-40).dp, y = 40.dp)
                        .size(12.dp)
                        .background(Color.White, CircleShape)
                )

                // Vertical line
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-34).dp, y = 46.dp)
                        .width(2.dp)
                        .height(140.dp)
                        .background(Color.White.copy(alpha = 0.8f))
                )
            }
        }
    }
}

@Composable
fun TimeframeChip(
    timeframe: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                if (isSelected) Color(0xFF3B3B3B) else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = timeframe,
            color = if (isSelected) Color.White else Color.Gray,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        )
    }
}

@Composable
fun PriceChart(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val chartColor = Color(0xFF00C851)
        val width = size.width
        val height = size.height

        val linearGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF151515), Color(0xFF08080A))
        )

        // Draw bars in background first
        drawChartBars(barColor = linearGradient)

        // Create sample chart path
        val path = Path()
        val points = listOf(
            Offset(0f, height * 0.8f),
            Offset(width * 0.15f, height * 0.7f),
            Offset(width * 0.3f, height * 0.75f),
            Offset(width * 0.45f, height * 0.6f),
            Offset(width * 0.6f, height * 0.65f),
            Offset(width * 0.75f, height * 0.4f),
            Offset(width * 0.9f, height * 0.3f)
        )

        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            val controlPoint1 = Offset(
                (points[i - 1].x + points[i].x) / 2,
                points[i - 1].y
            )
            val controlPoint2 = Offset(
                (points[i - 1].x + points[i].x) / 2,
                points[i].y
            )
            path.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                points[i].x, points[i].y
            )
        }

        drawPath(
            path = path,
            color = chartColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

fun DrawScope.drawChartBars(barColor: Brush) {
    val barWidth = 40.dp.toPx()
    val spacing = 20.dp.toPx()
    val totalWidth = size.width
    val barCount = (totalWidth / (barWidth + spacing)).toInt()

    val barHeights = listOf(0.3f, 0.6f, 0.4f, 0.8f, 0.7f, 0.5f, 0.9f, 0.6f)

    for (i in 0 until barCount) {
        val x = i * (barWidth + spacing)
        val heightRatio = barHeights.getOrElse(i % barHeights.size) { 0.5f }
        val barHeight = size.height * heightRatio
        val y = size.height - barHeight

        drawRect(
            brush = barColor,
            topLeft = Offset(x, y),
            size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
        )
    }
}

data class ChartPoint(val timestamp: Long, val value: Double)
enum class TimeFrame { HOUR, DAY, WEEK, MONTH, YEAR }

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun PriceChartCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1A1A))
                .padding(16.dp)
        ) {
            PriceChartCard(
                priceChart = emptyList(),
                selectedTimeframe = TimeFrame.MONTH,
                onTimeframeSelected = {},
                formatCurrency = { "₹ ${it.toInt()}" },
                getTimeframeDisplayName = { it.name }
            )
        }
    }
}