package com.hectoruiz.data.repositories

import com.hectoruiz.data.models.CurrencyResponseApiModel
import com.hectoruiz.data.models.FeeResponseApiModel
import com.hectoruiz.data.models.toModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CryptoRepositoryImplTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var remoteDataSource: CryptoRemoteDataSource

    private val cryptoRepositoryImpl by lazy { CryptoRepositoryImpl(remoteDataSource) }

    @Test
    fun `error retrieving the exchange rate from repository`() {
        val errorMessage = "Error"
        val errorResult = Result.failure<CurrencyResponseApiModel>(Throwable(errorMessage))
        coEvery { remoteDataSource.getExchangeRate(any()) } returns errorResult

        val result = runBlocking { cryptoRepositoryImpl.getExchangeRate("eur") }

        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(errorMessage, it.message)
        }
    }

    @Test
    fun `success retrieving the exchange rate from repository`() {
        val response = CurrencyResponseApiModel()
        val successResult = Result.success(response)
        coEvery { remoteDataSource.getExchangeRate(any()) } returns successResult

        val result = runBlocking { cryptoRepositoryImpl.getExchangeRate("eur") }

        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(response.toModel(), it)
        }
    }

    @Test
    fun `error retrieving the fee from repository`() {
        val errorMessage = "Error"
        val errorResult = flowOf(Result.failure<FeeResponseApiModel>(Throwable(errorMessage)))
        coEvery { remoteDataSource.getFee() } returns errorResult

        val result = runBlocking { cryptoRepositoryImpl.getFee().first() }

        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(errorMessage, it.message)
        }
    }

    @Test
    fun `success retrieving the fee from repository`() {
        val response = FeeResponseApiModel()
        val successResult = flowOf(Result.success(response))
        coEvery { remoteDataSource.getFee() } returns successResult

        val result = runBlocking { cryptoRepositoryImpl.getFee().first() }

        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(response.toModel(), it)
        }
    }
}