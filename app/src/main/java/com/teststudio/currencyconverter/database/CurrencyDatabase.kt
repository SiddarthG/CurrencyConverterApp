package com.teststudio.currencyconverter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teststudio.currencyconverter.database.dao.CurrencyListDao
import com.teststudio.currencyconverter.database.dao.LatestValueDao
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import com.teststudio.currencyconverter.database.entity.LatestValueEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Database(entities = [CurrencyListEntity::class, LatestValueEntity::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyListDao() : CurrencyListDao
    abstract fun latestValueDao() : LatestValueDao

    companion object {
        const val DATABASE_VERSION = 1

        const val DATABASE_NAME = "CurrencyDatabase"
        @Volatile private var instance: CurrencyDatabase? = null

        private var mutex = Mutex()

        class Callback: RoomDatabase.Callback()

        fun getInstance(context: Context) : CurrencyDatabase {
            return runBlocking {
                instance ?: mutex.withLock {
                    instance ?: buildDatabase(context).also { instance = it }
                }
            }
        }

        private fun buildDatabase(context: Context): CurrencyDatabase {
            return Room.databaseBuilder(context.applicationContext,
            CurrencyDatabase::class.java, DATABASE_NAME)
                .addCallback(Callback())
                .build()
        }

    }
}