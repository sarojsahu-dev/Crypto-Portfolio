package com.app.cryptoportfolio.domain.usecase

import java.math.BigDecimal
import javax.inject.Inject

class FormatCurrencyUseCase @Inject constructor() {
    operator fun invoke(amount: BigDecimal, currency: String): String {
        return when (currency) {
            "INR" -> "₹${formatNumber(amount)}"
            "BTC" -> "${formatCrypto(amount)} BTC"
            "ETH" -> "${formatCrypto(amount)} ETH"
            "LTC" -> "${formatCrypto(amount)} LTC"
            else -> "${formatNumber(amount)} $currency"
        }
    }

    private fun formatNumber(amount: BigDecimal): String {
        return when {
            amount >= BigDecimal("10000000") -> String.format("%.2fM", amount.divide(BigDecimal("1000000")))
            amount >= BigDecimal("100000") -> String.format("%.1fL", amount.divide(BigDecimal("100000")))
            amount >= BigDecimal("1000") -> String.format("%.2fK", amount.divide(BigDecimal("1000")))
            else -> String.format("%.2f", amount)
        }
    }

    private fun formatCrypto(amount: BigDecimal): String {
        return String.format("%.6f", amount)
    }
}