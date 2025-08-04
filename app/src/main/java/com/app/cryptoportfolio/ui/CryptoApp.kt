package com.app.cryptoportfolio.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.cryptoportfolio.navigation.CryptoNavHost
import com.app.cryptoportfolio.presentation.components.BottomNavigationBar
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import kotlinx.coroutines.delay


@Composable
fun CryptoApp(appState: AppState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .systemBarsPadding()
    ) {
        AppContent(appState = appState)
    }
}

@Composable
private fun AppContent(
    appState: AppState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CryptoNavHost(
            modifier = Modifier
                .fillMaxSize(),
            appState = appState
        )

        if (appState.shouldShowBottomBar) {
            BottomBar(
                appState = appState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun BottomBar(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        startAnimation = true
    }

    AnimatedVisibility(
        visible = startAnimation,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ),
        modifier = modifier
    ) {
        BottomNavigationBar(
            destinations = appState.topLevelDestinations,
            currentDestination = appState.currentTopLevelDestination(),
            onNavigate = appState::navigateToTopLevelDestination,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        )
    }
}
