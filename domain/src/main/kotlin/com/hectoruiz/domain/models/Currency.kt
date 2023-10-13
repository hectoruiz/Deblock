package com.hectoruiz.domain.models

import com.hectoruiz.domain.Currency

data class CurrencyModel(
    val currency: Currency,
)

data class FeeModel(
    val gasPrice: Double,
)