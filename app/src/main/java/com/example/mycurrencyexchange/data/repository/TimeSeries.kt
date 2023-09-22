package com.example.mycurrencyexchange.data.repository

import com.squareup.moshi.Json

data class TimeSeries(
    val startDate: String,
    val base: String,
    val success: Boolean,
    val endDate: String,
    val timeseries: Boolean,
    val rates: Map<String, Map<String, Double>>
)

