package com.hectoruiz.deblock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hectoruiz.ui.exchange.ExchangeScreen
import com.hectoruiz.ui.exchange.ExchangeViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = START_DESTINATION) {
        composable(route = START_DESTINATION) {
            val exchangeViewModel = hiltViewModel<ExchangeViewModel>()
            val exchangeUiState by exchangeViewModel.exchangeUiState.collectAsStateWithLifecycle()

            ExchangeScreen(
                exchangeUiState = exchangeUiState,
                onSwitchCurrency = { exchangeViewModel.switchCurrency(it) },
                onAllCurrencies = { exchangeViewModel.getAllRates() },
                onSelectCurrency = { exchangeViewModel.selectedCurrency(it) },
                onSendMoney = { println("Sending ${it.amount} of ${it.currency.name}") }
            )
        }
    }
}

private const val START_DESTINATION = "exchange"
