package com.example.mycurrencyexchange.ui.currencies_screen

import android.icu.util.Calendar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycurrencyexchange.data.entries.CurrenciesDynamic
import com.example.mycurrencyexchange.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class CurrenciesViewModel @Inject constructor(private val repository: CurrencyRepository): ViewModel() {

    val currencies = MutableLiveData<List<HolderItem>>()

    init {
        viewModelScope.launch{
            currencies.value = getHolderItems()
        }
    }

    fun searchByCurrencyName(name: String){
        viewModelScope.launch{
            currencies.value = getHolderItems().filter { it.name.contains(name.uppercase(Locale.ROOT)) }
        }
    }

    private suspend fun getHolderItems(): List<HolderItem> {

        val yesterday = getYesterdayDate()
        val today = getTodayDate()
        val timeSeriesEntry = repository.getTimeseries(yesterday, today, "UAH")
        val pastValue: Map<String, Double> = timeSeriesEntry.rates[yesterday]!!
        val lastValue: Map<String, Double> = timeSeriesEntry.rates[today]!!
        val change = lastValue.map { (key, value) ->
            val newVal = ((pastValue.getValue(key) - lastValue.getValue(key)) / lastValue.getValue(key)) * 100
            val dynamic = when{
                newVal > 0 -> CurrenciesDynamic.UP
                newVal < 0 -> CurrenciesDynamic.DOWN
                newVal == 0.0 -> CurrenciesDynamic.EQUAL

                else -> {throw IllegalArgumentException()}
            }
            HolderItem(key, newVal, dynamic)
        }
        return change
    }

    fun getTodayDate(): String{
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }

    fun getYesterdayDate(): String{
        val c: Date = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }
}

data class HolderItem(
    val name: String,
    val change: Double,
    val dynamic: CurrenciesDynamic
)