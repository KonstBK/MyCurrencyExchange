package com.example.mycurrencyexchange.di

import android.content.Context
import androidx.room.Room
import com.example.mycurrencyexchange.data.repository.CurrencyRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, CurrencyRoomDatabase::class.java, "currencies.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: CurrencyRoomDatabase) = db.currencyDao()
}