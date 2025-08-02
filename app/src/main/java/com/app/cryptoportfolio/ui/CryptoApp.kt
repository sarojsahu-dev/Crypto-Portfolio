package com.app.cryptoportfolio.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app.cryptoportfolio.navigation.CryptoNavHost
import com.app.cryptoportfolio.presentation.components.BottomNavigationPlaceholder
import com.app.cryptoportfolio.ui.theme.BackgroundDark
import kotlinx.coroutines.delay


@Composable
fun CryptoApp(appState: AppState) {
    AppContent(
        appState = appState
    )
}

@Composable
private fun AppContent(
    appState: AppState
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = BackgroundDark,
        content = { paddingValues ->
            CryptoNavHost(
                modifier = Modifier.padding(paddingValues),
                appState = appState
            )
        },
        bottomBar = {
            BottomBar(appState)
        }
    )
}

@Composable
private fun BottomBar(appState: AppState) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        startAnimation = true
    }

    AnimatedVisibility(
        visible = startAnimation && appState.shouldShowBottomBar,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            Column(
                modifier = Modifier.navigationBarsPadding()
            ) {
                BottomNavigationPlaceholder(
                    destinations = appState.topLevelDestinations,
                    currentDestination = appState.currentTopLevelDestination(),
                    onNavigate = appState::navigateToTopLevelDestination,
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding()
                )
            }
        }
    )
}

