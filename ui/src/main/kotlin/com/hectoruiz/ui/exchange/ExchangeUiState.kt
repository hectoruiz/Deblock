package com.hectoruiz.ui.exchange

import com.hectoruiz.domain.Currency
import com.hectoruiz.domain.ErrorState

data class ExchangeUiState(
    val error: ErrorState = ErrorState.NoError,
    val loading: Boolean = true,
    val fee: Double = 0.0,
    val mainCurrency: CurrencyUiModel = CurrencyUiModel(currency = Currency.Dollar, amount = 0.0),
    val secondaryCurrency: Currency = Currency.Ethereum,
    val allCurrencies: List<Currency> = emptyList(),
)
