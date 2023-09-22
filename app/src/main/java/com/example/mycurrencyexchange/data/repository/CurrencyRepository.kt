package com.example.mycurrencyexchange.data.repository

import com.example.mycurrencyexchange.domain.ICurrencyRepository
import com.example.mycurrencyexchange.data.network.CurrencyAPIService
import com.example.mycurrencyexchange.data.network.CurrencySymbols
import com.example.mycurrencyexchange.data.network.entries.TimeSeriesEntry
import javax.inject.Inject


class CurrencyRepository @Inject constructor(private val service: CurrencyAPIService) :
    ICurrencyRepository {


    override suspend fun getSymbols(): CurrencySymbols {
        return service.getCurrency()
    }


    override suspend fun getRecentRate(
        from: String,
        to: List<String>
    ): Map<String, Double> {
        return service.getRecentRate(from, to)
    }

    override suspend fun getPastRate(date: String): Map<String, Double> {
        return service.getPastRate(date)
    }

    override suspend fun getTimeseries(
        startDate: String,
        endDate: String,
        from: String
    ): TimeSeries {
        return service.getTimeseries(startDate, endDate, from).toTimeSeries()
    }
}

fun TimeSeriesEntry.toTimeSeries(): TimeSeries {
    return TimeSeries(
        startDate = startDate,
        base = base,
        success = success,
        endDate = endDate,
        timeseries = timeseries,
        rates = rates
    )
}