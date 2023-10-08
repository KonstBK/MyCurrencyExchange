package com.example.mycurrencyexchange.ui.currencies_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycurrencyexchange.data.entries.CurrenciesDynamic
import com.example.mycurrencyexchange.domain.CurrencyItem
import com.example.mycurrencyexchange.domain.use_case.GetCurrenciesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class CurrenciesViewModel @Inject constructor(private val getCurrenciesItem: GetCurrenciesItem): ViewModel() {

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
        return getCurrenciesItem.getHolderItems(null).map { it.toHolderItem() }
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