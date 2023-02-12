package com.teststudio.currencyconverter.hilt

import com.teststudio.currencyconverter.data.preferences.PreferenceManager
import com.teststudio.currencyconverter.database.dao.CurrencyListDao
import com.teststudio.currencyconverter.database.dao.LatestValueDao
import com.teststudio.currencyconverter.network.NetworkRepository
import com.teststudio.currencyconverter.repository.CurrencyConversionRepositoryImpl
import com.teststudio.currencyconverter.repository.CurrencyListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object HiltModule {

    @Provides
    @ActivityRetainedScoped
    fun providesCurrencyConversionRepoImpl(dao: LatestValueDao, preferenceManager: PreferenceManager, networkRepository: NetworkRepository) : CurrencyConversionRepositoryImpl = CurrencyConversionRepositoryImpl(dao, preferenceManager, networkRepository)

    @Provides
    @ActivityRetainedScoped
    fun providesCurrencyListRepositoryImpl(networkRepository: NetworkRepository, preferenceManager: PreferenceManager, currencyListDao: CurrencyListDao) : CurrencyListRepositoryImpl = CurrencyListRepositoryImpl(networkRepository, preferenceManager, currencyListDao)

}