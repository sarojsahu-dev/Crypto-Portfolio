package com.app.cryptoportfolio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.cryptoportfolio.navigation.TopLevelDestination
import com.app.cryptoportfolio.ui.theme.PrimaryBlue
import com.app.cryptoportfolio.ui.theme.SurfaceCard
import com.app.cryptoportfolio.ui.theme.TextPrimary
import com.app.cryptoportfolio.ui.theme.TextSecondary

@Composable
fun BottomNavigation(
    destinations: List<TopLevelDestination>,
    currentDestination: TopLevelDestination?,
    onNavigate: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(SurfaceCard),
        containerColor = SurfaceCard,
        contentColor = TextPrimary
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination == destination

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            if (isSelected) destination.selectedIcon else destination.selectedIcon
                        ),
                        contentDescription = null,
                        tint = if (isSelected) PrimaryBlue else TextSecondary
                    )
                },
                label = {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) PrimaryBlue else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                },
                selected = isSelected,
                onClick = { onNavigate(destination) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}


@Composable
fun BottomNavigationPlaceholder(
    destinations: List<TopLevelDestination>,
    currentDestination: TopLevelDestination?,
    onNavigate: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(SurfaceCard),
        containerColor = SurfaceCard,
        contentColor = TextPrimary
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination == destination

            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) PrimaryBlue else TextSecondary.copy(alpha = 0.3f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (destination) {
                                TopLevelDestination.Analytics -> "📊"
                                TopLevelDestination.Exchange -> "🔄"
                                TopLevelDestination.Record -> "📝"
                                TopLevelDestination.Wallet -> "💳"
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                label = {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) PrimaryBlue else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                },
                selected = isSelected,
                onClick = { onNavigate(destination) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}