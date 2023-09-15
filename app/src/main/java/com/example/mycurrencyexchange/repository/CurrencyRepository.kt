package com.example.mycurrencyexchange.repository

import com.example.mycurrencyexchange.network.CurrencyAPIService
import com.example.mycurrencyexchange.network.CurrencySymbols
import com.example.mycurrencyexchange.network.entries.TimeSeriesEntry
import javax.inject.Inject


class CurrencyRepository @Inject constructor(private val service: CurrencyAPIService): ICurrencyRepository {


     override suspend fun getSymbols(): CurrencySymbols{
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
    ): TimeSeriesEntry {
        return service.getTimeseries(startDate, endDate, from)
    }
}

interface ICurrencyRepository{

    suspend fun getSymbols(): CurrencySymbols

    suspend fun getRecentRate(from: String, to:List<String>): Map<String, Double>

    suspend fun getPastRate(date: String): Map<String, Double>

    suspend fun getTimeseries(startDate: String, endDate: String, from: String): TimeSeriesEntry


}