package com.teststudio.currencyconverter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teststudio.currencyconverter.database.entity.CurrencyListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyListDao {
    @Query("SELECT * FROM currencies")
    fun getCurrencyList() : Flow<List<CurrencyListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list : List<CurrencyListEntity>)
}