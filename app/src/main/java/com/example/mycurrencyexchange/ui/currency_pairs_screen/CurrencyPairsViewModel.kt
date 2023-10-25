package com.example.mycurrencyexchange.ui.currency_pairs_screen

import android.util.Log
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
class CurrencyPairsViewModel @Inject constructor(private val getCurrenciesItem: GetCurrenciesItem): ViewModel() {
    val pairs = MutableLiveData<List<CurrencyPairsItem>>()
    val pairsLoading = MutableLiveData<Boolean>()
    private val pairsError = SingleLiveEvent<String>()

    fun searchByCurrencyName(name: String){
        viewModelScope.launch {
            //subscribeOn(name.uppercase(Locale.ROOT))
            //фильтруем по имени
            val filteredPairs = pairs.value!!.filter { it.pairName.contains(name.uppercase(Locale.ROOT)) }

            // Обновляем список валютных пар в UI
            pairs.postValue(filteredPairs)

            Log.d("CurrencyPairsViewModel", "searchByCurrencyName() called with filter: $name")
        }
    }
     fun subscribeOn(selected:String) {
        //val items = getCurrenciesItem
         CoroutineScope(Dispatchers.IO).launch {
             getCurrenciesItem.getHolderItems(selected).collectLatest {
                 when (it){
                     is Resource.Error -> pairsError.postValue(it.message!!)
                     is Resource.Loading -> pairsLoading.postValue(true)
                     is Resource.Success -> {
                         pairs.postValue(it.data?.map { it.toCurrencyPairsItem() })
                         pairsLoading.postValue(false)
                     }
                 }
             }
         }
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

