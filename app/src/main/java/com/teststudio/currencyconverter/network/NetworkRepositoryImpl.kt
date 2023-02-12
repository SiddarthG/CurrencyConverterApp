package com.teststudio.currencyconverter.network

import com.teststudio.currencyconverter.BuildConfig
import com.teststudio.currencyconverter.network.response.CurrencyListResponse
import com.teststudio.currencyconverter.network.response.LatestRatesResponse
import retrofit2.Response
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(private val networkService: NetworkService) : NetworkRepository {

    override suspend fun getCurrencies(): Response<CurrencyListResponse> {
        return networkService.getCurrencies()
    }

    override suspend fun getLatestValue() : Response<LatestRatesResponse> {
        return networkService.getLatestValue(BuildConfig.APP_ID)
    }
}