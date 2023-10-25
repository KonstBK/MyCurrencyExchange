package com.example.mycurrencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.mycurrencyexchange.data.repository.CurrencyRoomDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var db: CurrencyRoomDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            CurrencyRoomDatabase::class.java, "currencies"
        ).build()
    }
}