package com.app.cryptoportfolio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState

fun NavController.navigateToAnalytics(navOptions: NavOptions? = null) =
    navigate(route = AnalyticsRoute, navOptions)

fun NavController.navigateToExchange(navOptions: NavOptions? = null) =
    navigate(route = ExchangeRoute, navOptions)

fun NavController.navigateToRecord(navOptions: NavOptions? = null) =
    navigate(route = RecordRoute, navOptions)

fun NavController.navigateToWallet(navOptions: NavOptions? = null) =
    navigate(route = WalletRoute, navOptions)

@Composable
fun NavController.currentTopLevelDestination(): TopLevelDestination? {
    val navBackStackEntry by currentBackStackEntryAsState()
    return TopLevelDestination.entries.find { destination ->
        navBackStackEntry?.destination?.hierarchy?.any {
            it.hasRoute(destination.route)
        } == true
    }
}

fun NavController.navigateToTopLevelDestination(
    destination: TopLevelDestination,
    navOptions: NavOptions? = null
) {
    val options = navOptions ?: androidx.navigation.navOptions {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (destination) {
        TopLevelDestination.Analytics -> navigateToAnalytics(options)
        TopLevelDestination.Exchange -> navigateToExchange(options)
        TopLevelDestination.Record -> navigateToRecord(options)
        TopLevelDestination.Wallet -> navigateToWallet(options)
    }
}