package com.hectoruiz.data.repositories

import com.hectoruiz.data.models.toModel
import com.hectoruiz.domain.models.CurrencyModel
import com.hectoruiz.domain.models.FeeModel
import com.hectoruiz.domain.repositories.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val cryptoRemoteDataSource: CryptoRemoteDataSource,
) : CryptoRepository {

    override suspend fun getExchangeRate(currentCurrency: String): Result<CurrencyModel> {
        return cryptoRemoteDataSource.getExchangeRate(currentCurrency).fold(
            onSuccess = {
                Result.success(it.toModel())
            },
            onFailure = {
                Result.failure(it)
            })
    }

    override fun getFee(): Flow<Result<FeeModel>> {
        return cryptoRemoteDataSource.getFee().map { result ->
            result.fold(
                onSuccess = {
                    Result.success(it.toModel())
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }.flowOn(Dispatchers.IO)
    }
}
