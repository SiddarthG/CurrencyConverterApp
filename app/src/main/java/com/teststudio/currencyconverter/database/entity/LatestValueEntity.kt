package com.teststudio.currencyconverter.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "latestvalues")
class LatestValueEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo("symbol") val symbol : String,
    @ColumnInfo("rate") val rate : Double
)