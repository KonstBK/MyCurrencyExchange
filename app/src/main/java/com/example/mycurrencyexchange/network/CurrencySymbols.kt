package com.example.mycurrencyexchange.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencySymbols(val symbols: Map<String, String>)

