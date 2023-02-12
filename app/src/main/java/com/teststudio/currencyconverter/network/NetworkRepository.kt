package com.teststudio.currencyconverter.network

import com.teststudio.currencyconverter.network.response.CurrencyListResponse
import com.teststudio.currencyconverter.network.response.LatestRatesResponse
import retrofit2.Response

interface NetworkRepository {
    suspend fun getCurrencies() : Response<CurrencyListResponse>
    suspend fun getLatestValue() : Response<LatestRatesResponse>
}