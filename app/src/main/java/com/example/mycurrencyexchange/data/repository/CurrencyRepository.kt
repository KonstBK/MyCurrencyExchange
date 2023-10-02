package com.example.mycurrencyexchange.data.repository

import android.util.Log
import com.example.mycurrencyexchange.domain.ICurrencyRepository
import com.example.mycurrencyexchange.data.network.CurrencyAPIService
import com.example.mycurrencyexchange.data.network.entries.TimeSeriesEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


class CurrencyRepository @Inject constructor(private val service: CurrencyAPIService, private val dao: CurrencyDao) :
    ICurrencyRepository {


    override suspend fun getTimeseries(
        startDate: String,
        endDate: String,
        from: String
    ): TimeSeries {
        return try {
            val entries = service.getTimeseries(startDate, endDate, from)
            entries.rates[startDate]?.entries?.map {
                Log.i("TAG", "map $it")
                CurrencyRateEntity(1, startDate, from, it.key, it.value)
            }?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertCurrencyRate(it)
                    Log.i("TAG", "let $it")
                }
            }
            entries.rates[endDate]?.entries?.map {
                CurrencyRateEntity(2, endDate, from, it.key, it.value)
            }?.let {
                CoroutineScope(Dispatchers.IO).launch {
                dao.insertCurrencyRate(it)
            }}
            return entries.toTimeSeries()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("Exception","${e.message} ")
            getLocalTimeseries(from, startDate, endDate)
        }
    }

    private suspend fun getLocalTimeseries(
        base: String,
        yesterdayDate: String,
        todayDate: String,

    ): TimeSeries {
        val res = mutableMapOf<String, Map<String, Double>>()
        val yesterday = dao.getCurrencyRate(yesterdayDate, base).first().associate { it.name to it.value }
        val today = dao.getCurrencyRate(todayDate, base).first().associate { it.name to it.value }
        res[todayDate] = today
        res[yesterdayDate] = yesterday
        val timeseries = TimeSeries(base, res)
        return timeseries
    }
    //Репозиторий это связующее звено для связи с бд и интернетом. Он должен обращаться к api, и возвращать данные, полученные с него, и тоже самое с бд.

}

fun TimeSeriesEntry.toTimeSeries() = TimeSeries(
    base = base,
    rates = rates
)




