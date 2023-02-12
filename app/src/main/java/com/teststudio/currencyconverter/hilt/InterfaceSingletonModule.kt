package com.teststudio.currencyconverter.hilt

import com.teststudio.currencyconverter.data.preferences.PreferenceManager
import com.teststudio.currencyconverter.data.preferences.PreferenceManagerImpl
import com.teststudio.currencyconverter.network.NetworkRepository
import com.teststudio.currencyconverter.network.NetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceSingletonModule {
    @Binds
    @Singleton
    abstract fun providesNetworkRepository(networkRepository: NetworkRepositoryImpl) : NetworkRepository

    @Binds
    @Singleton
    abstract fun providesPreferenceManager(preferenceManager: PreferenceManagerImpl) : PreferenceManager
}