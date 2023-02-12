package com.teststudio.currencyconverter.util

import com.teststudio.currencyconverter.data.preferences.PreferenceManager
import com.teststudio.currencyconverter.data.preferences.PreferenceManagerImpl
import com.teststudio.currencyconverter.network.Constants

class AppUtils {
    companion object {
        fun shouldFetchFromRemote(
            preferenceManager: PreferenceManager,
            prefKey: String
        ) : Boolean {
            var prevFetchTime : Long? = null
            if (prefKey == PreferenceManagerImpl.PREF_KEY_PREV_FETCH_TIME_LATEST_VALUES) {
                prevFetchTime = preferenceManager.getPrevFetchTimeForLatestValues()
            } else {
                prevFetchTime = preferenceManager.getPrevFetchTimeForCurrencyList()
            }

            if (prevFetchTime == null) {
                return true
            } else {
                val timeElapsed = System.currentTimeMillis() - (prevFetchTime.toLong())
                if (timeElapsed >= Constants.TTL) {
                    return true
                }
            }
            return false
        }
    }
}