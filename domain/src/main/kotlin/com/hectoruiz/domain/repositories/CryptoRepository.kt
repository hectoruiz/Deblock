package com.hectoruiz.domain.repositories

import com.hectoruiz.domain.models.CurrencyModel
import com.hectoruiz.domain.models.FeeModel
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    suspend fun getExchangeRate(currentCurrency: String): Result<CurrencyModel>

    fun getFee(): Flow<Result<FeeModel>>
}