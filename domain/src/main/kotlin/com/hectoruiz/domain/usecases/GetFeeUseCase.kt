package com.hectoruiz.domain.usecases

import com.hectoruiz.domain.models.FeeModel
import com.hectoruiz.domain.repositories.CryptoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeeUseCase @Inject constructor(private val cryptoRepository: CryptoRepository) {

    fun getFee(): Flow<Result<FeeModel>> = cryptoRepository.getFee()
}