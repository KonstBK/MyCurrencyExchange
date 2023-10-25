package com.example.mycurrencyexchange.ui.currencies_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycurrencyexchange.data.entries.CurrenciesDynamic
import com.example.mycurrencyexchange.domain.CurrencyItem
import com.example.mycurrencyexchange.domain.use_case.GetCurrenciesItem
import com.example.mycurrencyexchange.util.Resource
import com.example.mycurrencyexchange.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class CurrenciesViewModel @Inject constructor(private val getCurrenciesItem: GetCurrenciesItem) :
    ViewModel() {

    val currencies = MutableLiveData<List<HolderItem>>()
    val currenciesLoading = MutableLiveData<Boolean>()
    val currenciesError = SingleLiveEvent<String>()

    init {
        subscribeOn()
    }

    fun searchByCurrencyName(name: String) {
        viewModelScope.launch {
            // Фильтруем список валют по имени
            val filteredCurrencies =
                currencies.value!!.filter { it.name.contains(name.uppercase(Locale.ROOT)) }

            // Обновляем список валют в UI
            currencies.postValue(filteredCurrencies)
        }

    }

    private fun subscribeOn() {
        CoroutineScope(Dispatchers.IO).launch {
            getCurrenciesItem.getHolderItems(null).collectLatest {
                //loading.postValue(true)
                when (it) {
                    is Resource.Error -> currenciesError.postValue(it.message!!)
                    is Resource.Loading -> currenciesLoading.postValue(true)
                    is Resource.Success -> {
                        currencies.postValue(it.data?.map { it.toHolderItem() })
                        currenciesLoading.postValue(false)
                    }
                }
            }
        }
    }
}

private fun CurrencyItem.toHolderItem() = HolderItem(
    name = name,
    change = change,
    dynamic = dynamic
)


data class HolderItem(
    val name: String,
    val change: Double,
    val dynamic: CurrenciesDynamic
)