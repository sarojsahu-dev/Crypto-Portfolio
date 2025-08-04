package com.app.cryptoportfolio.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.cryptoportfolio.R
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data object BaseRoute

@Serializable
data object AnalyticsRoute

@Serializable
data object ExchangeRoute

@Serializable
data object RecordRoute

@Serializable
data object WalletRoute

enum class TopLevelDestination(
    @DrawableRes val selectedIcon: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
) {
    Analytics(
        selectedIcon = R.drawable.ic_analytics_filled,
        titleTextId = R.string.analytics,
        route = AnalyticsRoute::class
    ),
    Exchange(
        selectedIcon = R.drawable.ic_exchange_filled,
        titleTextId = R.string.exchange,
        route = ExchangeRoute::class
    ),
    Record(
        selectedIcon = R.drawable.ic_record_filled,
        titleTextId = R.string.record,
        route = RecordRoute::class
    ),
    Wallet(
        selectedIcon = R.drawable.ic_wallet_filled,
        titleTextId = R.string.wallet,
        route = WalletRoute::class
    )
}