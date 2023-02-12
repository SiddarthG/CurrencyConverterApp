package com.teststudio.currencyconverter.network

import com.teststudio.currencyconverter.network.response.CurrencyListResponse
import com.teststudio.currencyconverter.network.response.LatestRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("currencies.json")
    suspend fun getCurrencies() : Response<CurrencyListResponse>

    @GET("latest.json")
    suspend fun getLatestValue(@Query("app_id") appId : String): Response<LatestRatesResponse>

}