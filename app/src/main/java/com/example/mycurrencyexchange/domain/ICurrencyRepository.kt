package com.example.mycurrencyexchange.domain

import com.example.mycurrencyexchange.data.repository.TimeSeries

interface ICurrencyRepository{
    suspend fun getTimeseries(startDate: String, endDate: String, from: String): TimeSeries
}