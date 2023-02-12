package com.teststudio.currencyconverter.hilt

import com.teststudio.currencyconverter.repository.CurrencyConversionRepository
import com.teststudio.currencyconverter.repository.CurrencyConversionRepositoryImpl
import com.teststudio.currencyconverter.repository.CurrencyListRepository
import com.teststudio.currencyconverter.repository.CurrencyListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class InterfaceActivityModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun providesCurrencyListRepo(currencyListRepositoryImpl: CurrencyListRepositoryImpl) : CurrencyListRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun providesCurrencyConversionRepo(currencyConversionRepoImpl: CurrencyConversionRepositoryImpl) : CurrencyConversionRepository
}