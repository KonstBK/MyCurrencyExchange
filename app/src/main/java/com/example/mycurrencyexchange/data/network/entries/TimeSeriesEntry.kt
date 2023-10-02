package com.example.mycurrencyexchange.data.network.entries

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeSeriesEntry(
    @Json(name = "start_date")
    val startDate: String,
    val base: String,
    val success: Boolean,
    @Json(name = "end_date")
    val endDate: String,
    val timeseries: Boolean,
    val rates: Map<String, Map<String, Double>>
)
