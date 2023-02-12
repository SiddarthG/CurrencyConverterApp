package com.teststudio.currencyconverter.repository

import com.teststudio.currencyconverter.common.Resource
import com.teststudio.currencyconverter.data.preferences.PreferenceManager
import com.teststudio.currencyconverter.data.preferences.PreferenceManagerImpl
import com.teststudio.currencyconverter.database.dao.CurrencyListDao
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import com.teststudio.currencyconverter.network.NetworkRepository
import com.teststudio.currencyconverter.util.AppUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class CurrencyListRepositoryImpl @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val preferenceManager: PreferenceManager,
    private val currencyListDao: CurrencyListDao,
) : CurrencyListRepository {

    private val _currencyList: MutableStateFlow<Resource<List<CurrencyListEntity>>> =
        MutableStateFlow(Resource.loading(null))
    override fun getCurrencyList(): Flow<Resource<List<CurrencyListEntity>>> = _currencyList

    override suspend fun getCurrenciesFromRemote() {
        _currencyList.emit(Resource.loading(null))
        val response = networkRepository.getCurrencies()
        if (response.isSuccessful) {
            if (response.body() == null) {
                _currencyList.emit(Resource.error("Network request failed", null))
            }
            val transformedData = transformToDbModel(response)
            transformedData?.let {
                currencyListDao.insertList(transformedData)
                preferenceManager.setPrevFetchTimeForCurrencyList(System.currentTimeMillis())
                getCurrenciesFromDb()
            }
        } else {
            _currencyList.emit(Resource.error(response.errorBody().toString(), null))
        }

    }

    private fun transformToDbModel(response: Response<HashMap<String, String>>): List<CurrencyListEntity>? {
        return response.body()?.toList()?.map {
            CurrencyListEntity(
                symbol = it.first,
                name = it.second
            )
        }
    }

    override suspend fun getCurrenciesFromDb() {
        currencyListDao.getCurrencyList().collect {
            _currencyList.emit(Resource.success(it))
        }
    }

    override suspend fun getCurrencies() {
        if (AppUtils.shouldFetchFromRemote(
                preferenceManager,
                PreferenceManagerImpl.PREF_KEY_PREV_FETCH_TIME_CURRENCY_LIST
            )) {
            getCurrenciesFromRemote()
        } else {
            getCurrenciesFromDb()
        }
    }


}
