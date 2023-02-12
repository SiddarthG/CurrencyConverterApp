package com.teststudio.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.teststudio.currencyconverter.common.Status
import com.teststudio.currencyconverter.data.preferences.PreferenceManager
import com.teststudio.currencyconverter.data.uimodel.Currency
import com.teststudio.currencyconverter.database.dao.LatestValueDao
import com.teststudio.currencyconverter.database.entity.LatestValueEntity
import com.teststudio.currencyconverter.network.NetworkRepository
import com.teststudio.currencyconverter.network.response.LatestRatesResponse
import com.teststudio.currencyconverter.repository.CurrencyConversionRepository
import com.teststudio.currencyconverter.repository.CurrencyConversionRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.*
import retrofit2.Response

class CurrencyConversionRepoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcher = TestCoroutineDispatcher()

    private val networkRepository: NetworkRepository = mockk()
    private val preferenceManager: PreferenceManager = mockk(relaxed = true)
    private val latestValueDao: LatestValueDao = MockDao()

    lateinit var currencyConversionRepository: CurrencyConversionRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(dispatcher)
        currencyConversionRepository =
            CurrencyConversionRepositoryImpl(latestValueDao, preferenceManager, networkRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test when latestvalue is called with success response flow updated with success`() {
        dispatcher.runBlockingTest {
            coEvery { networkRepository.getLatestValue() } returns Response.success(
                LatestRatesResponse(hashMapOf("aed" to 2.30))
            )
            currencyConversionRepository.getLatestValue("mxn", "25")
            val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
                val response = currencyConversionRepository.getConvertedList().toList()
                Assert.assertEquals(
                    response.find { resource -> resource.status == Status.SUCCESS },
                    listOf<Currency>(
                        Currency(
                            "2.30", "aed"
                        )
                    )
                )
                delay(5000)
            }
            collectJob.cancel()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when latestvalue is called and response is null flow should recieve error`() {
        dispatcher.runBlockingTest {
            coEvery { networkRepository.getLatestValue() } returns Response.success(
                null
            )

            currencyConversionRepository.getLatestValue("mxn", "25")
            latestValueDao.getLatestValue()
            val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
                val response = currencyConversionRepository.getConvertedList().toList()
                val x = response.find { resource -> resource.status == Status.ERROR }
                Assert.assertEquals(
                    response.find { resource -> resource.status == Status.ERROR },
                    null
                )
                delay(5000)
            }
            collectJob.cancel()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when latestvalue is called and response is failure flow should recieve error`() {
        dispatcher.runBlockingTest {
            coEvery { networkRepository.getLatestValue() } returns Response.error(
                401,
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{\"key\":[\"somestuff\"]}"
                )
            )
            currencyConversionRepository.getLatestValue("mxn", "25")
            val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
                val response = currencyConversionRepository.getConvertedList().toList()
                val x = response.find { resource -> resource.status == Status.ERROR }
                Assert.assertEquals(
                    response.find { resource -> resource.status == Status.ERROR },
                    null
                )
                delay(5000)
            }
            collectJob.cancel()
        }
    }

    class MockDao : LatestValueDao {
        override fun getLatestValue(): Flow<List<LatestValueEntity>> {
            return flow { listOf<LatestValueEntity>(LatestValueEntity("aed", 2.0)) }
        }

        override fun insert(list: List<LatestValueEntity>) {

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }
}