package com.teststudio.currencyconverter.data.preferences

import android.content.SharedPreferences
import javax.inject.Inject


class PreferenceManagerImpl @Inject constructor(private val mPrefs : SharedPreferences) : PreferenceManager {
    companion object {
        const val PREF_KEY_PREV_FETCH_TIME_CURRENCY_LIST = "PREF_KEY_PREV_FETCH_TIME_CURRENCY_LIST"
        const val PREF_KEY_PREV_FETCH_TIME_LATEST_VALUES = "PREF_KEY_PREV_FETCH_TIME_LATEST_VALUES"
        private const val NULL_INDEX = -1L
    }

    override fun getPrevFetchTimeForCurrencyList(): Long? {
        val prevFetchTime = mPrefs.getLong(PREF_KEY_PREV_FETCH_TIME_CURRENCY_LIST, NULL_INDEX)
        return if (prevFetchTime == NULL_INDEX) null else prevFetchTime
    }

    override fun setPrevFetchTimeForCurrencyList(time: Long?) {
        val value = time ?: NULL_INDEX
        mPrefs.edit().putLong(PREF_KEY_PREV_FETCH_TIME_CURRENCY_LIST, value).apply()
    }

    override fun getPrevFetchTimeForLatestValues(): Long? {
        val prevFetchTime = mPrefs.getLong(PREF_KEY_PREV_FETCH_TIME_LATEST_VALUES, NULL_INDEX)
        return if (prevFetchTime == NULL_INDEX) null else prevFetchTime
    }

    override fun setPrevFetchTimeForLatestValues(time: Long?) {
        val value = time ?: NULL_INDEX
        mPrefs.edit().putLong(PREF_KEY_PREV_FETCH_TIME_LATEST_VALUES, value).apply()
    }

}