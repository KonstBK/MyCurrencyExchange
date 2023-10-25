package com.example.mycurrencyexchange.domain

import com.example.mycurrencyexchange.util.Resource
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository{
    suspend fun getTimeseries(startDate: String, endDate: String, from: String): Flow<Resource<TimeSeries>>
}