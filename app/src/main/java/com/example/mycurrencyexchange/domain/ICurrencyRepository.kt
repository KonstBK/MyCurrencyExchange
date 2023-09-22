package com.example.mycurrencyexchange.domain

import com.example.mycurrencyexchange.data.network.CurrencySymbols
import com.example.mycurrencyexchange.data.repository.TimeSeries

interface ICurrencyRepository{

    suspend fun getSymbols(): CurrencySymbols

    suspend fun getRecentRate(from: String, to:List<String>): Map<String, Double>

    suspend fun getPastRate(date: String): Map<String, Double>

    suspend fun getTimeseries(startDate: String, endDate: String, from: String): TimeSeries
}