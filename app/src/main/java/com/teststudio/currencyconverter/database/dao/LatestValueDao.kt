package com.teststudio.currencyconverter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teststudio.currencyconverter.database.entity.LatestValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LatestValueDao {
    @Query("SELECT * FROM latestvalues")
    fun getLatestValue() : Flow<List<LatestValueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list : List<LatestValueEntity>)
}