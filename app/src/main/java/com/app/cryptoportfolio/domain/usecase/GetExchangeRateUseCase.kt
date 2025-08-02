package com.app.cryptoportfolio.domain.usecase

import com.app.cryptoportfolio.domain.model.ExchangeRate
import com.app.cryptoportfolio.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(fromCurrency: String, toCurrency: String): Flow<ExchangeRate> =
        repository.getExchangeRate(fromCurrency, toCurrency)
}