package com.teststudio.currencyconverter

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.teststudio.currencyconverter.database.CurrencyDatabase
import com.teststudio.currencyconverter.database.dao.CurrencyListDao
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrencyListDaoTest {
    private lateinit var database: CurrencyDatabase
    private lateinit var currencyListDao: CurrencyListDao
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyDatabase::class.java
        ).allowMainThreadQueries().build()

        currencyListDao = database.currencyListDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertCurrencyList_returnsTrue() = runBlocking {
        val currency = CurrencyListEntity("aed", "AED")


        currencyListDao.insertList(listOf(currency))

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            currencyListDao.getCurrencyList().collect {
                assertEquals(it, listOf(currency))
                latch.countDown()

            }
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}