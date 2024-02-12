package com.karpovich.findmykidstest.app.data.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory {
    companion object {
        private const val BASE_URL = "https://api.github.com/"
        //private const val TOKEN = "YOUR_TOKEN"

    }
    /*private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $TOKEN")
                .build()
            chain.proceed(request)
        }
        .build()*/

    val apiService:ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}