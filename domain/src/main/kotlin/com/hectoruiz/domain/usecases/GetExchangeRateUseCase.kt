package com.hectoruiz.domain.usecases

import com.hectoruiz.domain.Currency
import com.hectoruiz.domain.models.CurrencyModel
import com.hectoruiz.domain.repositories.CryptoRepository
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(private val cryptoRepository: CryptoRepository) {

    suspend fun getExchangeRate(currentCurrency: Currency): Result<CurrencyModel> =
        cryptoRepository.getExchangeRate(currentCurrency.name.lowercase())
}