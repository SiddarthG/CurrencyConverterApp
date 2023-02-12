package com.teststudio.currencyconverter.data.preferences

interface PreferenceManager {
    fun getPrevFetchTimeForCurrencyList(): Long?
    fun setPrevFetchTimeForCurrencyList(time: Long?)
    fun getPrevFetchTimeForLatestValues(): Long?
    fun setPrevFetchTimeForLatestValues(time: Long?)
}