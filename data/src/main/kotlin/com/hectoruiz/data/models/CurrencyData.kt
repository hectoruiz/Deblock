package com.hectoruiz.data.models

import com.hectoruiz.domain.Currency
import com.hectoruiz.domain.models.CurrencyModel
import com.hectoruiz.domain.models.FeeModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponseApiModel(
    val ethereum: Map<String, Double>? = null,
)

@Serializable
data class FeeResponseApiModel(
    @SerialName("result")
    val result: FeeApiModel? = null,
)

@Serializable
data class FeeApiModel(
    @SerialName("FastGasPrice")
    val gasPrice: Double? = null,
)


fun CurrencyResponseApiModel?.toModel(): CurrencyModel {
    val currency = when (this?.ethereum?.entries?.first()?.key) {
        Currency.Dollar.name.lowercase() -> Currency.Dollar
        Currency.Euro.name.lowercase() -> Currency.Euro
        Currency.Pound.name.lowercase() -> Currency.Pound
        else -> Currency.Dollar
    }
    currency.exchangeRate = this?.ethereum?.entries?.first()?.value ?: 0.0
    return CurrencyModel(currency)
}

fun FeeResponseApiModel?.toModel() =
    FeeModel(gasPrice = (21000 * (this?.result?.gasPrice ?: 0.0)) / 100000000)