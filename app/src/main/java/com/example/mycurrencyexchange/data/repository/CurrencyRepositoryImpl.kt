package com.example.mycurrencyexchange.data.repository

import android.icu.util.Calendar
import android.util.Log
import com.example.mycurrencyexchange.data.network.CurrencyAPIService
import com.example.mycurrencyexchange.domain.ICurrencyRepository
import com.example.mycurrencyexchange.domain.TimeSeries
import com.example.mycurrencyexchange.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CurrencyRepositoryImpl @Inject constructor(
    private val service: CurrencyAPIService,
    private val dao: CurrencyDao
) :
    ICurrencyRepository {


    override suspend fun getTimeseries(
        startDate: String,
        endDate: String,
        from: String
    ): Flow<Resource<TimeSeries>> {
        return flow {
            try {
                emit(Resource.Loading())

                val entries = service.getTimeseries(startDate, endDate, from)
                entries.rates.forEach {
                    val date = it.key
                    it.value.map {
                        CurrencyRateEntity(
                            date = date,
                            base = from,
                            name = it.key,
                            value = it.value
                        )
                    }.let {
                        dao.insertCurrencyRate(it)
                        deleteOldRates()
                        val localTimeSeries = getLocalTimeseries(from, startDate, endDate)
                        emit(Resource.Success(localTimeSeries))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("TAG", "${e.message} ")
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }.flowOn(Dispatchers.IO)
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
        return TimeSeries(base, res)
    }

    private fun deleteOldRates() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -2)
        val twoDaysAgo: Long = calendar.timeInMillis
        dao.deleteOlderThanTwoDays(twoDaysAgo)
    }
    //Репозиторий это связующее звено для связи с бд и интернетом. Он должен обращаться к api, и возвращать данные, полученные с него, и тоже самое с бд.
}




