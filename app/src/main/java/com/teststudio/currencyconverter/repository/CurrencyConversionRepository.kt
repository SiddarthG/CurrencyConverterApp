package com.teststudio.currencyconverter.repository

import com.teststudio.currencyconverter.common.Resource
import com.teststudio.currencyconverter.data.uimodel.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyConversionRepository {
    suspend fun getLatestValue(currency: String, amt: String)
    fun getConvertedList() : Flow<Resource<List<Currency>>>
}