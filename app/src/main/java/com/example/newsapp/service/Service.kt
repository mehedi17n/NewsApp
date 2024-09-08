package com.example.newsapp.service

import com.example.newsapp.data.NewsResponse
import retrofit2.Call
import retrofit2.http.GET


interface ServiceApi {

        @GET("everything?q=apple&from=2024-09-02&to=2024-09-02&sortBy=popularity&apiKey=7d65984496e64ef5924ae49657184d83")
        suspend fun getNewsArticle(): NewsResponse

}