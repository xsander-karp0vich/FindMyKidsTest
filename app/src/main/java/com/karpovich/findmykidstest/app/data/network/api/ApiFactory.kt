package com.karpovich.findmykidstest.app.data.network.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory {
    companion object {
        private const val BASE_URL = "https://api.github.com/"
    }
    val apiService:ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}