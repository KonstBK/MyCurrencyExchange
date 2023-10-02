package com.example.mycurrencyexchange.data.repository

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "currencies")
data class CurrencyRateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: String,
    val base: String,
    val name: String,
    val value: Double
)

