package com.example.mycurrencyexchange.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies WHERE date=:date AND base=:base")
    fun getCurrencyRate(date: String, base: String): Flow<List<CurrencyRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyRate(entity: List<CurrencyRateEntity>)

    @Query("DELETE FROM currencies WHERE date < :twoDaysAgo")
    fun deleteOlderThanTwoDays(twoDaysAgo: Long): Void
}