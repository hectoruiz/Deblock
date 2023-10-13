package com.hectoruiz.domain

object Constants {
    const val WALLET_BALANCE_ETHEREUM = 10
    const val MAX_DIGITS = 10
    const val PARAM_IDS = "ids"
    const val PARAM_CURRENCIES = "vs_currencies"
    const val ETHEREUM_CURRENCY = "ethereum"
    const val FEE_URL = "https://api.etherscan.io/api?module=gastracker&action=gasoracle"
    const val FEE_RECALL_TIMEOUT = 5000L
    const val ANIMATION_DURATION = 1000
}

sealed class Currency(val symbol: String, val name: String, var exchangeRate: Double) {
    data object Euro : Currency("€", "EUR", 0.0)
    data object Dollar : Currency("$", "USD", 0.0)
    data object Pound : Currency("£", "GBP", 0.0)
    data object Ethereum : Currency("Ξ", "ETH", 0.0)
}