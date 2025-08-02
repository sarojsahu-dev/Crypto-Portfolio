package com.app.cryptoportfolio.domain.usecase

import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPortfolioUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(): Flow<Portfolio> = repository.getPortfolio()
}