package com.example.mycurrencyexchange.data.repository

import android.util.Log
import com.example.mycurrencyexchange.domain.ICurrencyRepository
import com.example.mycurrencyexchange.data.network.CurrencyAPIService
import com.example.mycurrencyexchange.data.network.entries.TimeSeriesEntry
import com.example.mycurrencyexchange.domain.TimeSeries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


class CurrencyRepository @Inject constructor(
    private val service: CurrencyAPIService,
    private val dao: CurrencyDao
) :
    ICurrencyRepository {


    override suspend fun getTimeseries(
        startDate: String,
        endDate: String,
        from: String
    ): TimeSeries {
        return try {
            val entries = service.getTimeseries(startDate, endDate, from)
            entries.rates.forEach {
                val date = it.key
                it.value.map {
                    CurrencyRateEntity(
                        date = date,
                        base = from,
                        name = it.key,
                        value = it.value)
                }.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.insertCurrencyRate(it)
                        Log.i("TAG", "let2 $it")
                    }.join()
                }
            }
            Log.i("TAG", "getTimeseries: $entries")
            return getLocalTimeseries(from, startDate, endDate)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("TAG", "${e.message} ")
            getLocalTimeseries(from, startDate, endDate)
        }
    }

    private suspend fun getLocalTimeseries(
        base: String,
        yesterdayDate: String,
        todayDate: String,

        ): TimeSeries {
        val res = mutableMapOf<String, Map<String, Double>>()
        val yesterday =
            dao.getCurrencyRate(yesterdayDate, base).first().associate { it.name to it.value }
        val today =
            dao.getCurrencyRate(todayDate, base).first().associate { it.name to it.value }
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




