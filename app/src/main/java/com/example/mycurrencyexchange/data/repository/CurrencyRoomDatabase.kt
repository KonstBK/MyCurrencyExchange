package com.example.mycurrencyexchange.data.repository


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CurrencyRateEntity::class],
    version = 1,
    exportSchema = false
)

abstract class CurrencyRoomDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao


}