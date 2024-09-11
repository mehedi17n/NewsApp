package com.example.newsapp.ui.home

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.api.Resource
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.data.Article
import com.example.newsapp.data.NewsResponse
import com.example.newsapp.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // Exposing read-only StateFlow to the outside, keeping MutableStateFlow internal
    private val _postResponse = MutableStateFlow<NewsResponse?>(null)
    val postResponse: StateFlow<NewsResponse?> = _postResponse

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val repository = PostRepository(RetrofitInstance.api)

    init {
        getPostList()
    }

    private fun getPostList() {
        viewModelScope.launch {
            repository.getNews().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true // Show loading
                    }

                    is Resource.Success -> {
                        _isLoading.value = false // Stop loading
                        _postResponse.value = resource.data // Set data
                    }

                    is Resource.Error -> {
                        _isLoading.value = false // Stop loading
                        _errorMessage.value = resource.message // Set error message



                    }
                }
            }
        }
    }

    fun onSearchText(query: String): List<Article?>? {
        // Filter the articles based on the source name
        val filteredArticles = _postResponse.value?.articles?.filter {
            it?.source?.name?.contains(query, ignoreCase = true) == true
        }

        return filteredArticles

    }
}