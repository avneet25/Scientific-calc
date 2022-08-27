package com.knotworking.numbers.api

import android.content.Context
import android.util.Log
import com.knotworking.numbers.Constants.INR
import com.knotworking.numbers.Constants.HKD
import com.knotworking.numbers.Constants.NZD
import com.knotworking.numbers.Constants.KPW
import com.knotworking.numbers.Constants.CNY
import com.knotworking.numbers.Constants.CAD

import com.knotworking.numbers.Constants.EUR
import com.knotworking.numbers.Constants.GBP
import com.knotworking.numbers.Constants.USD
import com.knotworking.numbers.database.DatabaseHelper
import com.knotworking.numbers.database.DatabaseHelperImpl

// This class will provide all the required information about currencies using open exchange rates api
// and also creates a data base to store and manipulate values
class CurrencyApi(context: Context) {
    private val TAG = CurrencyApi::class.java.simpleName

    // Representational state transfer is used for creating Web services. REST Web services allow the requesting systems
    // to access and manipulate textual representations of Web resources by using a uniform and predefined set of stateless operations
    private val service: CurrencyService = RestClient.createService(CurrencyService::class.java)

    // This creates the database required to handle date from the stated website
    private val databaseHelper: DatabaseHelper = DatabaseHelperImpl(context)

    // This method is used to get live updates from exchange rates website using api and uses different method to handle the situations
    fun getExchangeRates() {
        service.getLatestExchangeRates().enqueue(object : retrofit2.Callback<ExchangeRatesResponse> {
            override fun onFailure(call: retrofit2.Call<ExchangeRatesResponse>, t: Throwable) {
                Log.e(TAG, t.message)
            }

            // when it gets response from websites for following currencies it saves them into created database
            override fun onResponse(call: retrofit2.Call<ExchangeRatesResponse>, response: retrofit2.Response<ExchangeRatesResponse>) {
                response.body()?.let {
                    it.rates = it.rates.filterKeys { currencyCode ->
                        when (currencyCode) {
                            USD, CAD, GBP, EUR, INR, HKD, NZD, KPW, CNY -> true
                            else -> false
                        }
                    }

                    Log.i(TAG, it.rates.toString())

                    databaseHelper.saveExchangeRates(it.rates)
                }
            }

        })
    }
}