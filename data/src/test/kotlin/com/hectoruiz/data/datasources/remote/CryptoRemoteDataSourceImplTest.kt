package com.hectoruiz.data.datasources.remote

import com.hectoruiz.data.api.remote.ApiClient
import com.hectoruiz.data.api.remote.ApiService
import com.hectoruiz.data.models.CurrencyResponseApiModel
import com.hectoruiz.data.models.FeeResponseApiModel
import com.hectoruiz.data.repositories.CryptoRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create

class CryptoRemoteDataSourceImplTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var apiClient: ApiClient

    @MockK
    private lateinit var retrofit: Retrofit

    @MockK
    private lateinit var apiService: ApiService

    private val cryptoRemoteDataSource: CryptoRemoteDataSource =
        CryptoRemoteDataSourceImpl(apiService)

    @Before
    fun setUp() {
        every { apiClient.retrofit } returns retrofit
        every { retrofit.create<ApiService>() } returns apiService
    }

    @Test
    fun `error retrieving the exchange rate from remote data source`() {
        val errorCode = 400
        val errorMessage = "BadRequest"
        val errorResponse =
            Response.error<CurrencyResponseApiModel>(errorCode, errorMessage.toResponseBody())
        coEvery { apiService.getExchangeRate(any(), any()) } returns errorResponse

        val result = runBlocking {
            cryptoRemoteDataSource.getExchangeRate("eur")
        }
        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(errorResponse.errorBody().toString(), it.message)
        }
    }

    @Test
    fun `success retrieving the exchange rate from remote data source`() {
        val currencyResponseApiModel = mockk<CurrencyResponseApiModel>()
        val successResponse = Response.success(currencyResponseApiModel)
        coEvery { apiService.getExchangeRate(any(), any()) } returns successResponse

        val result = runBlocking {
            cryptoRemoteDataSource.getExchangeRate("eur")
        }
        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(currencyResponseApiModel, it)
        }
    }

    @Test
    fun `success retrieving the exchange rate with null body from remote data source`() {
        coEvery { apiService.getExchangeRate(any(), any()) } returns Response.success(null)

        val result = runBlocking {
            cryptoRemoteDataSource.getExchangeRate("eur")
        }
        assertTrue(result.isFailure)
    }

    @Test
    fun `error retrieving the fee from remote data source`() {
        val errorCode = 400
        val errorMessage = "BadRequest"
        val errorResponse =
            Response.error<FeeResponseApiModel>(errorCode, errorMessage.toResponseBody())
        coEvery { apiService.getCurrentFee(any()) } returns errorResponse

        val result = runBlocking {
            cryptoRemoteDataSource.getFee().first()
        }

        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(errorResponse.errorBody().toString(), it.message)
        }
    }

    @Test
    fun `success retrieving the fee from remote data source`() {
        val feeResponseApiModel = mockk<FeeResponseApiModel>()
        val successResponse = Response.success(feeResponseApiModel)
        coEvery { apiService.getCurrentFee(any()) } returns successResponse

        val result = runBlocking {
            cryptoRemoteDataSource.getFee().first()
        }

        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(feeResponseApiModel, it)
        }
    }

    @Test
    fun `success retrieving the fee with null body from remote data source`() {
        coEvery { apiService.getCurrentFee(any()) } returns Response.success(null)

        val result = runBlocking {
            cryptoRemoteDataSource.getFee().first()
        }

        assertTrue(result.isFailure)
    }
}