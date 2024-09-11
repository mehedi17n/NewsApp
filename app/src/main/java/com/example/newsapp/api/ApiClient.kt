package com.example.newsapp.api

import com.example.newsapp.service.ServiceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ServiceApi by lazy {
        retrofit.create(ServiceApi::class.java)
    }
}