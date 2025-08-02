package com.app.cryptoportfolio.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.cryptoportfolio.navigation.TopLevelDestination
import com.app.cryptoportfolio.navigation.currentTopLevelDestination
import com.app.cryptoportfolio.navigation.navigateToTopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState {
    return remember(navController, coroutineScope) {
        AppState(
            navController = navController
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController
) {

    @Composable
    fun currentTopLevelDestination(): TopLevelDestination? {
        return navController.currentTopLevelDestination()
    }

    val topLevelDestinations = TopLevelDestination.entries

    val shouldShowBottomBar: Boolean
        @Composable get() = currentTopLevelDestination() != null

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        navController.navigateToTopLevelDestination(destination)
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}