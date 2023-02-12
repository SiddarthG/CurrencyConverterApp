package com.teststudio.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.teststudio.currencyconverter.common.Status
import com.teststudio.currencyconverter.data.preferences.PreferenceManager
import com.teststudio.currencyconverter.database.dao.CurrencyListDao
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import com.teststudio.currencyconverter.network.NetworkRepository
import com.teststudio.currencyconverter.network.response.CurrencyListResponse
import com.teststudio.currencyconverter.repository.CurrencyListRepository
import com.teststudio.currencyconverter.repository.CurrencyListRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.junit.*
import retrofit2.Response

class CurrencyListRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcher = TestCoroutineDispatcher()

    private val networkRepository: NetworkRepository = mockk()
    private val preferenceManager: PreferenceManager = mockk(relaxed = true)
    private val currencyListDao: CurrencyListDao = mockk(relaxed = true)

    lateinit var currencyListRepository: CurrencyListRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(dispatcher)
        currencyListRepository =
            CurrencyListRepositoryImpl(networkRepository, preferenceManager, currencyListDao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test when getCurrencies is called currencyList updated with success`() {
        dispatcher.runBlockingTest {
            coEvery { networkRepository.getCurrencies() } returns Response.success(
                CurrencyListResponse(mapOf("aed" to "aed"))
            )
            currencyListRepository.getCurrencies()
            val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
                val response = currencyListRepository.getCurrencyList().toList()
                Assert.assertEquals(
                    response.find { resource -> resource.status == Status.SUCCESS },
                    listOf<CurrencyListEntity>(
                        CurrencyListEntity(
                            "aed", "aed"
                        )
                    )
                )
                delay(5000)
            }
            collectJob.cancel()


        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }


}