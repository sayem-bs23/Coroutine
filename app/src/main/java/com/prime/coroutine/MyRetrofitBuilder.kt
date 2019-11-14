package com.prime.coroutine

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



object MyRetrofitBuilder {
    const val BASE_URL = "https://open-api.xyz"


    val retrofitBuilder: Retrofit.Builder by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService:ApiService by lazy{

        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }


}