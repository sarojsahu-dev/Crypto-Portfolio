package com.app.cryptoportfolio.domain.usecase

import com.app.cryptoportfolio.domain.model.ChartTimeframe
import com.app.cryptoportfolio.domain.model.PriceChart
import com.app.cryptoportfolio.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPriceChartUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    operator fun invoke(cryptoId: String, timeframe: ChartTimeframe): Flow<PriceChart> =
        repository.getPriceChart(cryptoId, timeframe)
}