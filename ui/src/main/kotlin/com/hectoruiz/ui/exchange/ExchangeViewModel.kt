package com.hectoruiz.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectoruiz.domain.Constants.FEE_RECALL_TIMEOUT
import com.hectoruiz.domain.Currency
import com.hectoruiz.domain.ErrorState
import com.hectoruiz.domain.models.FeeModel
import com.hectoruiz.domain.usecases.GetExchangeRateUseCase
import com.hectoruiz.domain.usecases.GetFeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CurrencyUiModel(
    val currency: Currency,
    val amount: Double,
)

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val getFeeUseCase: GetFeeUseCase,
) : ViewModel() {

    private val coroutineHandler = CoroutineExceptionHandler { _, throwable ->
        error.update { ErrorState.NetworkError(throwable.message ?: "") }
        loading.update { false }
    }

    private val loading = MutableStateFlow(true)
    private val error = MutableStateFlow<ErrorState>(ErrorState.NoError)
    private val fee = MutableStateFlow(FeeModel(0.0))
    private val exchangeCurrencies =
        MutableStateFlow<Pair<CurrencyUiModel, Currency>>(
            Pair(
                CurrencyUiModel(currency = Currency.Dollar, amount = 0.0),
                Currency.Ethereum
            )
        )
    private val allCurrencies = MutableStateFlow<List<Currency>>(emptyList())
    val exchangeUiState = combine(
        loading,
        error,
        fee,
        exchangeCurrencies,
        allCurrencies
    ) { loading, error, fee, exchangeCurrencies, allCurrencies ->
        ExchangeUiState(
            loading = loading,
            error = error,
            fee = fee.gasPrice,
            mainCurrency = exchangeCurrencies.first,
            secondaryCurrency = exchangeCurrencies.second,
            allCurrencies = allCurrencies
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExchangeUiState())

    init {
        getFee()
        getInitialRate(Currency.Dollar)
    }

    private fun getInitialRate(currentCurrency: Currency) {
        viewModelScope.launch(coroutineHandler) {
            loading.update { true }
            getExchangeRateUseCase.getExchangeRate(currentCurrency).fold(
                onSuccess = { result ->
                    exchangeCurrencies.update { currencies ->
                        currencies.copy(
                            first = CurrencyUiModel(
                                currency = result.currency,
                                amount = 0.0,
                            )
                        )
                    }
                    allCurrencies.update { mutableListOf(result.currency) + allCurrencies.value }
                },
                onFailure = { throwable ->
                    error.update { ErrorState.NetworkError(message = throwable.message ?: "") }
                }
            )
            loading.update { false }
        }
    }

    private fun getFee() {
        viewModelScope.launch(coroutineHandler) {
            while (true) {
                getFeeUseCase.getFee().collect { result ->
                    result.fold(
                        onSuccess = {
                            fee.update { fee -> fee.copy(gasPrice = it.gasPrice) }
                        },
                        onFailure = { throwable ->
                            error.update {
                                ErrorState.NetworkError(
                                    message = throwable.message ?: ""
                                )
                            }
                        }
                    )
                }
                delay(FEE_RECALL_TIMEOUT)
            }
        }
    }

    fun switchCurrency(currentAmount: Double) {
        exchangeCurrencies.update { currencies ->
            currencies.copy(
                first = CurrencyUiModel(currency = currencies.second, amount = currentAmount),
                second = currencies.first.currency
            )
        }
    }

    fun selectedCurrency(currency: Currency) {
        exchangeCurrencies.update { currencies ->
            currencies.copy(
                CurrencyUiModel(currency = currency, amount = 0.0),
                Currency.Ethereum
            )
        }
    }

    fun getAllRates() {
        if (!allCurrencies.value.contains(Currency.Euro)) getExchangeRate(Currency.Euro)
        if (!allCurrencies.value.contains(Currency.Pound)) getExchangeRate(Currency.Pound)
    }

    private fun getExchangeRate(currency: Currency) {
        viewModelScope.launch(coroutineHandler) {
            loading.update { true }
            getExchangeRateUseCase.getExchangeRate(currency).fold(
                onSuccess = { result ->
                    allCurrencies.update { mutableListOf(result.currency) + allCurrencies.value }
                },
                onFailure = { throwable ->
                    error.update { ErrorState.NetworkError(message = throwable.message ?: "") }
                }
            )
            loading.update { false }
        }
    }
}
