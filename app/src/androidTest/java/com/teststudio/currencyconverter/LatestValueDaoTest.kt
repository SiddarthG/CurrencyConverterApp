package com.teststudio.currencyconverter

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.teststudio.currencyconverter.database.CurrencyDatabase
import com.teststudio.currencyconverter.database.dao.LatestValueDao
import com.teststudio.currencyconverter.database.entity.LatestValueEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
@SmallTest
class LatestValueDaoTest {
    private lateinit var database: CurrencyDatabase
    private lateinit var latestValueDao: LatestValueDao
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyDatabase::class.java
        ).allowMainThreadQueries().build()

        latestValueDao = database.latestValueDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertLatestValue_returnsTrue() = runBlocking {
        val latestValue = LatestValueEntity("aed", 2.3456)


        latestValueDao.insert(listOf(latestValue))

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            latestValueDao.getLatestValue().collect {
                assertEquals(it, listOf(latestValue))
                latch.countDown()

            }
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}