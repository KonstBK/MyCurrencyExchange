package com.example.mycurrencyexchange.domain.use_case

import android.icu.util.Calendar
import android.util.Log
import com.example.mycurrencyexchange.data.entries.CurrenciesDynamic
import com.example.mycurrencyexchange.data.repository.CurrencyRepository
import com.example.mycurrencyexchange.domain.CurrencyItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetCurrenciesItem @Inject constructor(private val repository: CurrencyRepository){

    suspend fun getHolderItems(selected: String?): List<CurrencyItem> {

        val yesterday = getYesterdayDate()
        val today = getTodayDate()
        val timeSeries = repository.getTimeseries(yesterday, today, selected ?: "UAH")
        Log.i("TAG", "getHolderItems: $timeSeries")
        val pastValue: Map<String, Double> = timeSeries.rates[yesterday]!!
        val lastValue: Map<String, Double> = timeSeries.rates[today]!!

        Log.i("TAG", "pastValue: $pastValue")
        Log.i("TAG", "lastValue: $lastValue")

        val change = lastValue.map { (key) ->
            val newVal = ((pastValue.getValue(key) - lastValue.getValue(key)) / lastValue.getValue(key)) * 100
            val dynamic = when{
                newVal > 0 -> CurrenciesDynamic.UP
                newVal < 0 -> CurrenciesDynamic.DOWN
                newVal == 0.0 -> CurrenciesDynamic.EQUAL

                else -> {throw IllegalArgumentException()}
            }
            if (selected == null) {
                CurrencyItem(key, newVal, dynamic)
            } else {
                CurrencyItem("$selected/$key", newVal, dynamic)
            }
        }
        Log.i("TAG", "change: $change")
        return change
    }

    private fun getTodayDate(): String{
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }

    private fun getYesterdayDate(): String{
        val c: Date = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }
}