package com.example.newsapp

import com.example.newsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface ServiceApi {

        @GET("everything?q=apple&from=2024-09-02&to=2024-09-02&sortBy=popularity&apiKey=7d65984496e64ef5924ae49657184d83")
        fun getNewsArticle(): Call<NewsResponse>

}