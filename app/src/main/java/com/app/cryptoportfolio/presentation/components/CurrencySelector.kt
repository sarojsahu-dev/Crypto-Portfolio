package com.app.cryptoportfolio.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.cryptoportfolio.ui.theme.SurfaceCard
import com.app.cryptoportfolio.ui.theme.TextPrimary

@Composable
fun CurrencySelector(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /* Show selector */ },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceCard
        )
    ) {
        Text(
            text = selectedCurrency,
            color = TextPrimary
        )
    }
}