package com.teststudio.currencyconverter.repository

import com.teststudio.currencyconverter.common.Resource
import com.teststudio.currencyconverter.data.preferences.PreferenceManager
import com.teststudio.currencyconverter.data.preferences.PreferenceManagerImpl
import com.teststudio.currencyconverter.data.uimodel.Currency
import com.teststudio.currencyconverter.database.dao.LatestValueDao
import com.teststudio.currencyconverter.database.entity.LatestValueEntity
import com.teststudio.currencyconverter.network.NetworkRepository
import com.teststudio.currencyconverter.network.response.LatestRatesResponse
import com.teststudio.currencyconverter.util.AppUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CurrencyConversionRepositoryImpl @Inject constructor(
    private val dao: LatestValueDao,
    private val preferenceManager: PreferenceManager,
    private val networkRepository: NetworkRepository
) : CurrencyConversionRepository {

    private val _convertedList: MutableStateFlow<Resource<List<Currency>>> =
        MutableStateFlow(Resource.loading(null))

    override fun getConvertedList(): Flow<Resource<List<Currency>>> = _convertedList

    override suspend fun getLatestValue(
        currency: String,
        amt: String
    ) {
        _convertedList.emit(Resource.loading(null))
        if (AppUtils.shouldFetchFromRemote(
                preferenceManager,
                PreferenceManagerImpl.PREF_KEY_PREV_FETCH_TIME_LATEST_VALUES
            )
        ) {
            val latestRatesResponse = networkRepository.getLatestValue()
            if (latestRatesResponse.isSuccessful) {
                if (latestRatesResponse.body() == null) {
                    _convertedList.emit(Resource.error("Network request Failed", null))
                } else {
                    transformToDbModel(latestRatesResponse.body())?.let { dao.insert(it) }
                }
            } else {
                _convertedList.emit(Resource.error("Network request Failed", null))
            }
        }
        dao.getLatestValue().collect {
            val convert = convertAsPerUserRequest(currency, amt, it)
            if (convert == null) {
                _convertedList.emit(Resource.error("Could not convert", null))
            } else {
                _convertedList.emit(Resource.success(convert))
            }
        }
    }

    private fun convertAsPerUserRequest(
        currency: String,
        amt: String,
        list: List<LatestValueEntity>
    ): List<Currency>? {
        val denominator = list.find { rateObject ->
            rateObject.symbol == currency
        }
        val usdValue = denominator?.let {
            amt.toDouble() / denominator.rate
        }
        if (usdValue != null) {
            return list.map {
                val rate = (usdValue * it.rate)
                val rounded = String.format("%.2f", rate)
                Currency(
                    rounded,
                    it.symbol
                )
            }
        }
        return null
    }

    private fun transformToDbModel(latestRatesResponse: LatestRatesResponse?): List<LatestValueEntity>? {
        return latestRatesResponse?.rates?.entries?.map {
            LatestValueEntity(
                it.key,
                it.value
            )
        }
    }
}