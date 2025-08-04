package com.app.cryptoportfolio.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.cryptoportfolio.navigation.TopLevelDestination
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import com.app.cryptoportfolio.ui.theme.PrimaryBlue
import com.app.cryptoportfolio.ui.theme.TintSelected
import com.app.cryptoportfolio.ui.theme.TintUnselected

@Composable
fun BottomNavigationBar(
    destinations: List<TopLevelDestination>,
    currentDestination: TopLevelDestination?,
    onNavigate: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(52.dp))
                .background(BackgroundDark)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            destinations.forEach { destination ->
                val isSelected = currentDestination == destination
                val background = if (isSelected)
                    Brush.verticalGradient(colors = listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8)))
                else
                    Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Transparent))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(44.dp))
                        .background(background)
                        .clickable { onNavigate(destination) }
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(destination.selectedIcon),
                            contentDescription = destination.name,
                            tint = if (isSelected) Color.White else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = destination.name,
                            color = if (isSelected) Color.White else Color.Gray,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 16.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { /* handle add */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color(0xFF3B82F6),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
