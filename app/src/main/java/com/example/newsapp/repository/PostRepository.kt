package com.example.newsapp.repository

import android.net.http.HttpException
import android.util.Log
import com.example.newsapp.api.Resource
import com.example.newsapp.api.RetrofitInstance.api
import com.example.newsapp.data.NewsResponse
import com.example.newsapp.service.ServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

//class PostRepository(private val api: ServiceApi) {
//    suspend fun getNews(): Flow<Resource<NewsResponse>> = flow {
//        Log.d("Repository", "getPosts")
//        try {
//            emit(Resource.Loading) // Emit loading state
//            val response = api.getNewsArticle() // Make the network request
//            emit(Resource.Success(response))
//        } catch (e: Exception) {
//            emit(Resource.Error(e.message ?: "Unknown Error")) // Emit error state
//        }
//    }.flowOn(Dispatchers.IO)
//
//}

class PostRepository(private val api: ServiceApi) {

    suspend fun getNews(): Flow<Resource<NewsResponse>> = flow {
        Log.d("Repository", "Fetching news articles...")
        try {
            emit(Resource.Loading) // Emit loading state
            val response = api.getNewsArticle() // Make the network request
            emit(Resource.Success(response))
        } catch (e: IOException) {
            Log.e("Repository", "Network error: ${e.message}")
            emit(Resource.Error("Network Error: Check your connection"))
        } catch (e: retrofit2.HttpException) {
            Log.e("Repository", "Server error: ${e.message}")
            emit(Resource.Error("Server Error: Unable to fetch data"))
        } catch (e: Exception) {
            Log.e("Repository", "Unknown error: ${e.message}")
            emit(Resource.Error("Unknown Error: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}
