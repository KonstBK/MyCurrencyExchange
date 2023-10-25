package com.example.mycurrencyexchange.domain


data class TimeSeries(
    val base: String,
    val rates: Map<String, Map<String, Double>>)
