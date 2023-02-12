package com.teststudio.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.teststudio.currencyconverter.common.Resource
import com.teststudio.currencyconverter.data.uimodel.Currency
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import com.teststudio.currencyconverter.repository.CurrencyConversionRepository
import com.teststudio.currencyconverter.repository.CurrencyListRepository
import com.teststudio.currencyconverter.viewmodel.CurrencyListViewModel
import io.mockk.MockKAnnotations
import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    val mockkRule = MockKRule(this)

    var currencyListRepository = MockRepo()
    var currencyConversionRepository = MockConversionRepo()
    lateinit var currencyListViewModel : CurrencyListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(dispatcher)
        currencyListViewModel = CurrencyListViewModel(currencyListRepository, currencyConversionRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }


    @Test
    fun `test when sync is called livedata is updated with success response`() {
        dispatcher.runBlockingTest {
            val result = Resource.success(
                listOf<CurrencyListEntity>()
            )
            currencyListViewModel.observerCurrencyList()
            currencyListViewModel.syncCurrencyList()

            // This isn't even needed.
            testScheduler.apply { advanceTimeBy(1000); runCurrent() }

            val liveDataResult = currencyListViewModel.currencyList.getOrAwaitValue(5)
            assertEquals(result.data, liveDataResult)
        }
    }

    @Test
    fun `test when latestvalue is called livedata is updated`() {
        dispatcher.runBlockingTest {
            val result = Resource.success(
                listOf<Currency>()
            )

            currencyListViewModel.observeCurrencyConversionResponse()
            currencyListViewModel.getLatestValue("aed", "23")

            // This isn't even needed.
            testScheduler.apply { advanceTimeBy(1000); runCurrent() }

            val liveDataResult = currencyListViewModel.currencyConversionResult.getOrAwaitValue(5)
            assertEquals(result.data, liveDataResult)
        }
    }

    @Test
    fun `test when currencyConversion gives error`() {
        dispatcher.runBlockingTest {
            val result = "Something went wrong"
            val repo = MockConversionErrorRepo()
            val vm = CurrencyListViewModel(currencyListRepository, repo)
            vm.observeCurrencyConversionResponse()
            vm.getLatestValue("qws", "2")

            testScheduler.apply { advanceTimeBy(1000); runCurrent() }

            val liveDataResult = vm.showSnackBar.getOrAwaitValue(5)
            assertEquals(result, liveDataResult)
        }
    }


    class MockRepo : CurrencyListRepository {
        private val _currencyList: MutableStateFlow<Resource<List<CurrencyListEntity>>> =
            MutableStateFlow(Resource.loading(null))
        override fun getCurrencyList(): Flow<Resource<List<CurrencyListEntity>>> = _currencyList

        override suspend fun getCurrenciesFromRemote() {
            TODO("Not yet implemented")
        }

        override suspend fun getCurrenciesFromDb() {
            TODO("Not yet implemented")
        }

        override suspend fun getCurrencies() {
            _currencyList.emit(Resource.success(listOf<CurrencyListEntity>()))
        }

    }

    class MockConversionRepo : CurrencyConversionRepository {
        private val _convertedList: MutableStateFlow<Resource<List<Currency>>> =
            MutableStateFlow(Resource.loading(null))

        override suspend fun getLatestValue(currency: String, amt: String) {
            _convertedList.emit(Resource.success(listOf<Currency>()))
        }

        override fun getConvertedList(): Flow<Resource<List<Currency>>> = _convertedList

    }

    class MockConversionErrorRepo : CurrencyConversionRepository {
        private val _convertedList: MutableStateFlow<Resource<List<Currency>>> =
            MutableStateFlow(Resource.loading(null))

        override suspend fun getLatestValue(currency: String, amt: String) {
            _convertedList.emit(Resource.error("", null))
        }

        override fun getConvertedList(): Flow<Resource<List<Currency>>> = _convertedList

    }
}