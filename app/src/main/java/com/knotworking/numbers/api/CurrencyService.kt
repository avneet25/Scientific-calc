package com.knotworking.numbers.api

import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyService {
// the iso code used to construct the currency.
    companion object {
        private const val APP_ID = "8d15d3595f7d4052a883f0df53c9871f"
    }

    @GET("latest.json")
    fun getLatestExchangeRates(@Query("app_id") appId: String = APP_ID): retrofit2.Call<ExchangeRatesResponse>

}
