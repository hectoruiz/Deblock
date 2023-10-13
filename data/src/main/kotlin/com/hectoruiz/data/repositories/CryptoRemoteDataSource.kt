package com.hectoruiz.data.repositories

import com.hectoruiz.data.models.CurrencyResponseApiModel
import com.hectoruiz.data.models.FeeResponseApiModel
import kotlinx.coroutines.flow.Flow

interface CryptoRemoteDataSource {

    suspend fun getExchangeRate(currentCurrency: String): Result<CurrencyResponseApiModel>

    fun getFee(): Flow<Result<FeeResponseApiModel>>
}