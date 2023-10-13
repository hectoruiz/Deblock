package com.hectoruiz.data.api.remote

import com.hectoruiz.data.models.CurrencyResponseApiModel
import com.hectoruiz.data.models.FeeResponseApiModel
import com.hectoruiz.domain.Constants.PARAM_CURRENCIES
import com.hectoruiz.domain.Constants.PARAM_IDS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("api/v3/simple/price")
    suspend fun getExchangeRate(
        @Query(PARAM_IDS) currency: String,
        @Query(PARAM_CURRENCIES) currentCurrency: String,
    ): Response<CurrencyResponseApiModel>


    @GET
    suspend fun getCurrentFee(@Url feeUrl: String): Response<FeeResponseApiModel>
}


