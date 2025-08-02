package com.app.cryptoportfolio.domain.usecase

import java.math.BigDecimal
import javax.inject.Inject

class CalculatePercentageChangeUseCase @Inject constructor() {
    operator fun invoke(currentValue: BigDecimal, previousValue: BigDecimal): Double {
        if (previousValue == BigDecimal.ZERO) return 0.0
        return currentValue.subtract(previousValue)
            .divide(previousValue, 4, java.math.RoundingMode.HALF_UP)
            .multiply(BigDecimal("100"))
            .toDouble()
    }
}