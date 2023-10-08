package com.example.mycurrencyexchange.ui.currency_pairs_screen

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
class CurrencyPairsViewModel @Inject constructor(private val getCurrenciesItem: GetCurrenciesItem): ViewModel() {
    val pairs = MutableLiveData<List<CurrencyPairsItem>>()

    fun searchByCurrencyName(name: String){
        viewModelScope.launch {
            getCurrencyPairsItem(name.uppercase(Locale.ROOT))
        }
    }
    suspend fun getCurrencyPairsItem(selected:String): List<CurrencyPairsItem> {
        val items = getCurrenciesItem.getHolderItems(selected).map{ it.toCurrencyPairsItem()}
        pairs.value = items
        return items
    }
}

private fun CurrencyItem.toCurrencyPairsItem() = CurrencyPairsItem(
    pairName = name,
    pairChange = change,
    pairDynamic = dynamic
)

data class CurrencyPairsItem(
    val pairName: String,
    val pairChange: Double,
    val pairDynamic: CurrenciesDynamic
)

