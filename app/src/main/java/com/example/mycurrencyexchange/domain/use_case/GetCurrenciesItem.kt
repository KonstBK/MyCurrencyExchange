package com.example.mycurrencyexchange.domain.use_case

import android.icu.util.Calendar
import android.util.Log
import com.example.mycurrencyexchange.data.entries.CurrenciesDynamic
import com.example.mycurrencyexchange.data.repository.CurrencyRepositoryImpl
import com.example.mycurrencyexchange.domain.CurrencyItem
import com.example.mycurrencyexchange.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetCurrenciesItem @Inject constructor(private val repository: CurrencyRepositoryImpl) {

    suspend fun getHolderItems(selected: String?): Flow<Resource<List<CurrencyItem>>> {

        val yesterday = getYesterdayDate()
        val today = getTodayDate()
        return repository.getTimeseries(yesterday, today, selected ?: "UAH").map {
            return@map when (it){
                is Resource.Error -> Resource.Error(it.message.orEmpty())
                is Resource.Loading -> Resource.Loading()
                is Resource.Success -> {val pastValue: Map<String, Double> = it.data!!.rates[yesterday]!!
                    val lastValue: Map<String, Double> = it.data.rates[today]!!

                    Log.i("TAG", "pastValue: $pastValue")
                    Log.i("TAG", "lastValue: $lastValue")

                    val change = lastValue.map { (key) ->
                        val newVal =
                            ((pastValue.getValue(key) - lastValue.getValue(key)) /
                                    lastValue.getValue(key)) * 100
                        val dynamic = when {
                            newVal > 0 -> CurrenciesDynamic.UP
                            newVal < 0 -> CurrenciesDynamic.DOWN
                            newVal == 0.0 -> CurrenciesDynamic.EQUAL

                            else -> {
                                throw IllegalArgumentException()
                            }
                        }
                        if (selected == null) {
                            CurrencyItem(key, newVal, dynamic)
                        } else {
                            CurrencyItem("$selected/$key", newVal, dynamic)
                        }
                    }
                    Log.i("TAG", "change: $change")
                    Resource.Success(change)
                }
            }


        }


    }

    private fun getTodayDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }

    private fun getYesterdayDate(): String {
        val c: Date = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }
}