package com.hectoruiz.domain

import com.hectoruiz.domain.models.FeeModel
import com.hectoruiz.domain.repositories.CryptoRepository
import com.hectoruiz.domain.usecases.GetFeeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetFeeUseCaseTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var cryptoRepository: CryptoRepository

    private val getFeeUseCase by lazy { GetFeeUseCase(cryptoRepository) }

    @Test
    fun `error retrieving the fee`() {
        val errorMessage = "Error!!"
        val errorResult = flowOf(Result.failure<FeeModel>(Throwable(errorMessage)))
        coEvery { cryptoRepository.getFee() } returns errorResult

        val result = runBlocking { getFeeUseCase.getFee().first() }

        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `success retrieving the fee `() {
        val feeModel = Result.success<FeeModel>(mockk())
        val successResult = flowOf(feeModel)
        coEvery { cryptoRepository.getFee() } returns successResult

        val result = runBlocking { getFeeUseCase.getFee().first() }

        assertTrue(result.isSuccess)
        assertEquals(feeModel, result)
    }
}
