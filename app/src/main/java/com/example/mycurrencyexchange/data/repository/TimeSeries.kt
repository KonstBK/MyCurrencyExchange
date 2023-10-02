package com.example.mycurrencyexchange.data.repository


data class TimeSeries(
    val base: String,
    val rates: Map<String, Map<String, Double>>)