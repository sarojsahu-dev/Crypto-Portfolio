package com.app.cryptoportfolio.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: String,
    val type: TransactionType,
    val cryptocurrency: Cryptocurrency,
    val amount: BigDecimal,
    val price: BigDecimal,
    val timestamp: LocalDateTime,
    val status: TransactionStatus
)

enum class TransactionType {
    BUY, SELL, SEND, RECEIVE
}

enum class TransactionStatus {
    PENDING, COMPLETED, FAILED
}