package com.hectoruiz.domain

import com.hectoruiz.domain.models.CurrencyModel
import com.hectoruiz.domain.repositories.CryptoRepository
import com.hectoruiz.domain.usecases.GetExchangeRateUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetExchangeRateUseCaseTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var cryptoRepository: CryptoRepository

    private val getExchangeRateUseCase by lazy { GetExchangeRateUseCase(cryptoRepository) }

    @Test
    fun `error retrieving the fee`() {
        val errorMessage = "Error!!"
        val errorResult = Result.failure<CurrencyModel>(Throwable(errorMessage))
        coEvery { cryptoRepository.getExchangeRate(any()) } returns errorResult

        val result = runBlocking { getExchangeRateUseCase.getExchangeRate(Currency.Euro) }

        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `success retrieving the fee `() {
        val successResult = Result.success<CurrencyModel>(mockk())
        coEvery { cryptoRepository.getExchangeRate(any()) } returns successResult

        val result = runBlocking { getExchangeRateUseCase.getExchangeRate(Currency.Euro) }

        assertTrue(result.isSuccess)
        assertEquals(successResult, result)
    }
}
