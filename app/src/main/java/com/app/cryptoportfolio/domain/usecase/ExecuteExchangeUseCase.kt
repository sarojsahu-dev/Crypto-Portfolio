package com.app.cryptoportfolio.domain.usecase

import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.ExchangeRate
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.domain.model.TransactionStatus
import com.app.cryptoportfolio.domain.model.TransactionType
import com.app.cryptoportfolio.domain.repository.CryptoRepository
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

class ExecuteExchangeUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke(
        fromCurrency: String,
        toCurrency: String,
        amount: BigDecimal,
        rate: ExchangeRate
    ): Result<Transaction> {
        return try {
            val transaction = Transaction(
                id = "txn_${System.currentTimeMillis()}",
                type = if (fromCurrency == "INR") TransactionType.BUY else TransactionType.SELL,
                cryptocurrency = Cryptocurrency(
                    id = toCurrency.lowercase(),
                    symbol = toCurrency,
                    name = toCurrency,
                    currentPrice = rate.rate,
                    priceChangePercentage24h = 0.0,
                    icon = toCurrency.lowercase()
                ),
                amount = amount,
                price = rate.rate,
                timestamp = LocalDateTime.now(),
                status = TransactionStatus.PENDING
            )

            repository.addTransaction(transaction)

            // Simulate processing time
            delay(2000)

            val completedTransaction = transaction.copy(status = TransactionStatus.COMPLETED)
            Result.success(completedTransaction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
