package com.knotworking.numbers.api

import com.knotworking.numbers.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// This object is used as a base to access information in the website using api
object RestClient {

    private const val BASE_URL = "https://openexchangerates.org/api/"

    fun <S> createService(serviceClass: Class<S>): S {
        val client: OkHttpClient
        //OkHTTP is an open source project designed to be an efficient HTTP client. It supports the SPDY protocol
        val clientBuilder = OkHttpClient.Builder()
        //Retrofit is a REST Client for Java and Android.
        // It makes it relatively easy to retrieve and upload JSON  via a REST based webservice
        val builder: Retrofit.Builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
// Interceptors are a powerful mechanism that can monitor, rewrite, and retry calls
        clientBuilder.addInterceptor(createLoggingInterceptor())
//Time in milliseconds that connectors will wait for another HTTP request before closing the connection.
// When not set, the connector's container-specific default will be used
        clientBuilder.connectTimeout(1, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES)
        client = clientBuilder.build()
        builder.baseUrl(BASE_URL)

        val retrofit = builder.client(client).build()
        return retrofit.create(serviceClass)
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        //An interceptor is the perfect place for request / response logging in OkHttp
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = when {
            (BuildConfig.DEBUG) -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

}