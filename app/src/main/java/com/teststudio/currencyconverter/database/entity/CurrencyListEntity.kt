package com.teststudio.currencyconverter.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
class CurrencyListEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "symbol") val symbol : String,
    @ColumnInfo(name = "name") val name : String
)