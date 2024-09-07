package com.example.newsapp

import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.RetrofitInstance
import com.example.newsapp.ui.home.NewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(emptyList()) // Initially empty list
        recyclerView.adapter = newsAdapter

        // Fetch news articles
        fetchNewsArticles()

        return view
    }

    private fun fetchNewsArticles() {
        // Using Retrofit to fetch news articles
        RetrofitInstance.api.getNewsArticle().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val articles = response.body()?.articles?.filterNotNull() ?: emptyList()
                    newsAdapter.updateArticles(articles) // Update adapter with fetched articles
                } else {
                    Toast.makeText(requireContext(), "Failed to load news", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("HomeFragment", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error fetching news", Toast.LENGTH_SHORT).show()
            }
        })
    }
}