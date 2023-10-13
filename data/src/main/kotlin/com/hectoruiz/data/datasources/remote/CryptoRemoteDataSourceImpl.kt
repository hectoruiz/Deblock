package com.hectoruiz.data.datasources.remote

import com.hectoruiz.data.api.remote.ApiService
import com.hectoruiz.data.models.CurrencyResponseApiModel
import com.hectoruiz.data.models.FeeResponseApiModel
import com.hectoruiz.data.repositories.CryptoRemoteDataSource
import com.hectoruiz.domain.Constants.ETHEREUM_CURRENCY
import com.hectoruiz.domain.Constants.FEE_URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CryptoRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    CryptoRemoteDataSource {

    override suspend fun getExchangeRate(currentCurrency: String): Result<CurrencyResponseApiModel> {
        val response = apiService.getExchangeRate(ETHEREUM_CURRENCY, currentCurrency)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(Throwable())
        } else {
            Result.failure(Throwable(response.errorBody().toString()))
        }
    }

    override fun getFee(): Flow<Result<FeeResponseApiModel>> = flow {
        val response = apiService.getCurrentFee(FEE_URL)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(Result.success(body))
            } else {
                emit(Result.failure(Throwable()))
            }
        } else {
            emit(Result.failure(Throwable(response.errorBody().toString())))
        }
    }
}