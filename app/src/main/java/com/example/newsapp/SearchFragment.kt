package com.example.newsapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.data.Article
import com.example.newsapp.data.NewsResponse
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.ui.home.MainViewModel
import com.example.newsapp.ui.home.NewsAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: MainViewModel
    private var articleList: List<Article> = emptyList() // Store all articles

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize views
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView)
        searchBar = view.findViewById(R.id.searchBar)
        progressBar = view.findViewById(R.id.progressBar)

        // Setup RecyclerView
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(emptyList()) // Initially, no articles
        searchRecyclerView.adapter = newsAdapter

        // Fetch news articles
//        fetchNewsArticles()

        // Listen for text changes in the search bar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                filterArticlesBySource(s.toString()) // Filter articles as user types
            val list = viewModel.onSearchText(s.toString())

                newsAdapter.updateArticles(list)

            }

            override fun afterTextChanged(s: Editable?) {}
        })

        handleLoading()

        lifecycleScope.launch {
            viewModel.postResponse.collect { response ->
                if (response != null) {
                    val articles = response.articles?.filterNotNull() ?: emptyList()
                    newsAdapter.updateArticles(articles)
                }
            }
        }

        return view
    }

    private fun handleLoading() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }


//    private fun fetchNewsArticles() {
//        showProgressBar()
//        // Using Retrofit to fetch news articles
//        RetrofitInstance.api.().enqueue(object : Callback<NewsResponse> {
//            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
//                hideProgressBar()
//                if (response.isSuccessful && response.body() != null) {
//                    articleList = response.body()?.articles?.filterNotNull() ?: emptyList()
//                    // Initially show all articles
//                    newsAdapter.updateArticles(articleList)
//                }
//            }
//
//            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
//                hideProgressBar()
//                // Handle error
//            }
//        })
//    }

    private fun filterArticlesBySource(query: String) {

        // Filter the articles based on the source name
        val filteredArticles = articleList.filter {
            it.source?.name?.contains(query, ignoreCase = true) == true
        }
        newsAdapter.updateArticles(filteredArticles)
    }
}
