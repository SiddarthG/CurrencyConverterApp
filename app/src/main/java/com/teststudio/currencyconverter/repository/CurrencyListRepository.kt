package com.teststudio.currencyconverter.repository

import com.teststudio.currencyconverter.common.Resource
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyListRepository {
    suspend fun getCurrenciesFromRemote()
    suspend fun getCurrenciesFromDb()
    suspend fun getCurrencies()
    fun getCurrencyList(): Flow<Resource<List<CurrencyListEntity>>>
}