package com.example.mycurrencyexchange.network

import com.example.mycurrencyexchange.network.entries.TimeSeriesEntry
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject


class CurrencyAPIService @Inject constructor() {

 private val BASE_URL = "https://currency-conversion-and-exchange-rates.p.rapidapi.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val original: Request = chain.request()

        // Настраиваем запросы
        val request: Request = original.newBuilder()
            .header("X-RapidAPI-Key", "d985cd9ab7mshf6d3824d1b68073p159545jsn0dc8d09d660d")
            .header("X-RapidAPI-Host", "currency-conversion-and-exchange-rates.p.rapidapi.com")
            .method(original.method, original.body)
            .build()
        chain.proceed(request)
    }).addNetworkInterceptor(loggingInterceptor)
    .build()



    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .baseUrl(BASE_URL)
        .build()

    //TODO create case if no internet

    private val api by lazy { retrofit.create(CurrencyApi::class.java) }

    suspend fun getCurrency(): CurrencySymbols {
        return api.getSymbols()
    }

    suspend fun getTimeseries(startDate: String, endDate: String, from: String): TimeSeriesEntry {
        return api.timeSeriesEndpoint(startDate, endDate, from)
    }

    suspend fun getRecentRate(from: String, to: List<String>): Map<String, Double> {
        return api.getRecentRate(from, to)
    }

    suspend fun getPastRate(date: String): Map<String, Double> {
        return api.getPastRate(date)
    }

}

interface CurrencyApi{

    @GET("currency")
    suspend fun getSymbols(): CurrencySymbols

    @GET("timeseries")
    suspend fun timeSeriesEndpoint(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("from") from: String
    ): TimeSeriesEntry

    @GET("recent_rate")
    suspend fun getRecentRate(
        @Query("from") from: String,
        @Query("to")to:List<String>): Map<String, Double>

    @GET("past_rate")
    suspend fun getPastRate(
        @Query("date")date: String): Map<String, Double>

}