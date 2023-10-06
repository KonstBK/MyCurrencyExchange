package com.example.mycurrencyexchange.domain

import com.example.mycurrencyexchange.data.entries.CurrenciesDynamic

data class CurrencyItem(
    val name: String,
    val change: Double,
    val dynamic: CurrenciesDynamic
)


