package com.app.cryptoportfolio.data.dto



import com.app.cryptoportfolio.domain.model.CryptoHolding
import com.app.cryptoportfolio.domain.model.Cryptocurrency
import com.app.cryptoportfolio.domain.model.Portfolio
import com.app.cryptoportfolio.domain.model.Transaction
import com.app.cryptoportfolio.domain.model.TransactionStatus
import com.app.cryptoportfolio.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
data class CryptocurrencyDto(
    val id: String,
    val symbol: String,
    val name: String,
    val current_price: String,
    val price_change_percentage_24h: Double,
    val image: String
)

data class PortfolioDto(
    val total_value: String,
    val total_change_percentage: Double,
    val holdings: List<CryptoHoldingDto>
)

data class CryptoHoldingDto(
    val cryptocurrency: CryptocurrencyDto,
    val amount: String,
    val current_value: String,
    val change_percentage: Double
)

data class TransactionDto(
    val id: String,
    val type: String,
    val cryptocurrency: CryptocurrencyDto,
    val amount: String,
    val price: String,
    val timestamp: String,
    val status: String
)

fun CryptocurrencyDto.toDomain(): Cryptocurrency {
    return Cryptocurrency(
        id = id,
        symbol = symbol,
        name = name,
        currentPrice = BigDecimal(current_price),
        priceChangePercentage24h = price_change_percentage_24h,
        icon = image
    )
}

fun PortfolioDto.toDomain(): Portfolio {
    return Portfolio(
        totalValue = BigDecimal(total_value),
        totalChangePercentage = total_change_percentage,
        holdings = holdings.map { it.toDomain() }
    )
}

fun CryptoHoldingDto.toDomain(): CryptoHolding {
    return CryptoHolding(
        cryptocurrency = cryptocurrency.toDomain(),
        amount = BigDecimal(amount),
        currentValue = BigDecimal(current_value),
        changePercentage = change_percentage
    )
}

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = id,
        type = TransactionType.valueOf(type.uppercase()),
        cryptocurrency = cryptocurrency.toDomain(),
        amount = BigDecimal(amount),
        price = BigDecimal(price),
        timestamp = LocalDateTime.parse(timestamp),
        status = TransactionStatus.valueOf(status.uppercase())
    )
}

fun Cryptocurrency.toDto(): CryptocurrencyDto {
    return CryptocurrencyDto(
        id = id,
        symbol = symbol,
        name = name,
        current_price = currentPrice.toString(),
        price_change_percentage_24h = priceChangePercentage24h,
        image = icon
    )
}

fun Portfolio.toDto(): PortfolioDto {
    return PortfolioDto(
        total_value = totalValue.toString(),
        total_change_percentage = totalChangePercentage,
        holdings = holdings.map { it.toDto() }
    )
}

fun CryptoHolding.toDto(): CryptoHoldingDto {
    return CryptoHoldingDto(
        cryptocurrency = cryptocurrency.toDto(),
        amount = amount.toString(),
        current_value = currentValue.toString(),
        change_percentage = changePercentage
    )
}