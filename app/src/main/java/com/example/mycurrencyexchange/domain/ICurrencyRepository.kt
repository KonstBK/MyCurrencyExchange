package com.example.mycurrencyexchange.domain

interface ICurrencyRepository{
    suspend fun getTimeseries(startDate: String, endDate: String, from: String): TimeSeries
}