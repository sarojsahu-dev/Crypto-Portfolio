package com.app.cryptoportfolio.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.app.cryptoportfolio.presentation.screen.AnalyticsScreen
import com.app.cryptoportfolio.presentation.screen.ExchangeScreen
import com.app.cryptoportfolio.presentation.screen.RecordScreen
import com.app.cryptoportfolio.presentation.screen.WalletScreen
import com.app.cryptoportfolio.ui.AppState

@Composable
fun CryptoNavHost(
    modifier: Modifier = Modifier,
    appState: AppState,
    navController: NavHostController = appState.navController,
    startDestination: Any = BaseRoute
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        navigation<BaseRoute>(startDestination = AnalyticsRoute) {

            analyticsSection()

            exchangeSection()

            recordSection()

            walletSection()
        }
    }
}


fun NavGraphBuilder.analyticsSection() {
    composable<AnalyticsRoute> {
        AnalyticsScreen()
    }
}

fun NavGraphBuilder.exchangeSection() {
    composable<ExchangeRoute> {
        ExchangeScreen(
            onBackClick = { }
        )
    }
}

fun NavGraphBuilder.recordSection() {
    composable<RecordRoute> {
        RecordScreen()
    }
}

fun NavGraphBuilder.walletSection() {
    composable<WalletRoute> {
        WalletScreen()
    }
}