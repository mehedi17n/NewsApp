package com.example.newsapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.RetrofitInstance
import com.example.newsapp.ui.home.NewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private var articleList: List<Article> = emptyList() // Store all articles

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize views
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView)
        searchBar = view.findViewById(R.id.searchBar)
        progressBar = view.findViewById(R.id.progressBar)

        // Setup RecyclerView
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(emptyList()) // Initially, no articles
        searchRecyclerView.adapter = newsAdapter

        // Fetch news articles
        fetchNewsArticles()

        // Listen for text changes in the search bar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterArticlesBySource(s.toString()) // Filter articles as user types
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }


    private fun fetchNewsArticles() {
        showProgressBar()
        // Using Retrofit to fetch news articles
        RetrofitInstance.api.getNewsArticle().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                hideProgressBar()
                if (response.isSuccessful && response.body() != null) {
                    articleList = response.body()?.articles?.filterNotNull() ?: emptyList()
                    // Initially show all articles
                    newsAdapter.updateArticles(articleList)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                hideProgressBar()
                // Handle error
            }
        })
    }

    private fun filterArticlesBySource(query: String) {
        // Filter the articles based on the source name
        val filteredArticles = articleList.filter {
            it.source?.name?.contains(query, ignoreCase = true) == true
        }
        newsAdapter.updateArticles(filteredArticles)
    }
}
