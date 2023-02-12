package com.teststudio.currencyconverter.hilt

import android.content.Context
import android.content.SharedPreferences
import com.teststudio.currencyconverter.common.AppConstants
import com.teststudio.currencyconverter.data.preferences.PreferenceManagerImpl
import com.teststudio.currencyconverter.database.CurrencyDatabase
import com.teststudio.currencyconverter.database.dao.CurrencyListDao
import com.teststudio.currencyconverter.database.dao.LatestValueDao
import com.teststudio.currencyconverter.network.Constants
import com.teststudio.currencyconverter.network.NetworkRepositoryImpl
import com.teststudio.currencyconverter.network.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    fun providesSharedPreferenceFileName() = AppConstants.preferenceFileName

    @Provides
    @Singleton
    fun providesRetrofitInstance() : Retrofit {
        return Retrofit.Builder().baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesNetworkService(retrofit: Retrofit) = retrofit.create(NetworkService::class.java)

    @Provides
    @Singleton
    fun providesNetworkRepositoryImpl(networkService: NetworkService) : NetworkRepositoryImpl = NetworkRepositoryImpl(networkService)

    @Provides
    @Singleton
    fun providesPreferenceManagerImpl(sharedPreferences: SharedPreferences) : PreferenceManagerImpl = PreferenceManagerImpl(sharedPreferences)

    @Provides
    @Singleton
    fun providesPreferences(@ApplicationContext context : Context) : SharedPreferences = context.getSharedPreferences(
        providesSharedPreferenceFileName(), Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesCurrencyDatabase(@ApplicationContext context: Context) : CurrencyDatabase {
        return CurrencyDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesCurrencyListDao(database: CurrencyDatabase) : CurrencyListDao {
        return database.currencyListDao()
    }

    @Provides
    @Singleton
    fun providesLatestValueDao(database: CurrencyDatabase) : LatestValueDao {
        return database.latestValueDao()
    }

}