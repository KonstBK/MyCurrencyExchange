package com.example.mycurrencyexchange.data.network

import com.example.mycurrencyexchange.data.network.entries.TimeSeriesEntry
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi{

    @GET("currency")
    suspend fun getSymbols(): CurrencySymbols

    @GET("timeseries")
    suspend fun timeSeriesEndpoint(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("from") from: String
    ): TimeSeriesEntry

    @GET("recent_rate")
    suspend fun getRecentRate(
        @Query("from") from: String,
        @Query("to")to:List<String>): Map<String, Double>

    @GET("past_rate")
    suspend fun getPastRate(
        @Query("date")date: String): Map<String, Double>

}