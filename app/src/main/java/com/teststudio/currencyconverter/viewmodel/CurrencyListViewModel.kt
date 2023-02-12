package com.teststudio.currencyconverter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teststudio.currencyconverter.common.Status
import com.teststudio.currencyconverter.data.uimodel.Currency
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import com.teststudio.currencyconverter.repository.CurrencyConversionRepository
import com.teststudio.currencyconverter.repository.CurrencyListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(private val currencyListRepository: CurrencyListRepository, private val currencyConversionRepository: CurrencyConversionRepository) : ViewModel() {

    private val _currencyConversionResult = MutableLiveData<List<Currency>?>()
    val currencyConversionResult : LiveData<List<Currency>?> = _currencyConversionResult

    private val _currencyList = MutableLiveData<List<CurrencyListEntity>?>()
    val currencyList : LiveData<List<CurrencyListEntity>?> = _currencyList

    private val _showProgress = MutableLiveData<Boolean>()
    val showProgress : LiveData<Boolean> = _showProgress

    private val _showSnackBar = MutableLiveData<String>()
    val showSnackBar : LiveData<String> = _showSnackBar

    init {
        observerCurrencyList()
        observeCurrencyConversionResponse()
    }

    fun observeCurrencyConversionResponse() {
        viewModelScope.launch {
            currencyConversionRepository.getConvertedList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        _showProgress.value = true
                    }

                    Status.ERROR -> {
                        _showProgress.value = false
                        _showSnackBar.value = "Something went wrong"
                    }

                    Status.SUCCESS -> {
                        _showProgress.value = false
                        _currencyConversionResult.value = it.data
                    }
                }
            }
        }
    }

    fun observerCurrencyList() {
        viewModelScope.launch {
            currencyListRepository.getCurrencyList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        _showProgress.value = true
                    }

                    Status.SUCCESS -> {
                        _showProgress.value = false
                        _currencyList.value = it.data
                    }

                    Status.ERROR -> {
                        _showProgress.value = false
                        _showSnackBar.value = "Something went wrong"
                    }
                }
            }
        }
    }

    fun syncCurrencyList() {
        viewModelScope.launch(Dispatchers.IO) {
            currencyListRepository.getCurrencies()
        }
    }

    fun getLatestValue(currency: String, amt: String) {
        _showProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            currencyConversionRepository.getLatestValue(currency, amt)
        }
    }

    fun validate(amt: String) : Boolean {
        if (amt.isNotEmpty()) {
            return try {
                amt.toDouble()
                true
            } catch(e: NumberFormatException) {
                false
            }
        }
        return false
    }
}