package com.app.cryptoportfolio.domain.usecase

import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = repository.getTransactions()
}