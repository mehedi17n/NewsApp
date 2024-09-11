package com.example.newsapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.data.NewsResponse
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.ui.home.MainViewModel
import com.example.newsapp.ui.home.NewsAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter with an empty list
        newsAdapter = NewsAdapter(emptyList())
        recyclerView.adapter = newsAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

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

    private fun handleError() {
        lifecycleScope.launch {
            viewModel.errorMessage.collect { msg ->
                if(msg!= null)
                 Toast.makeText(requireContext(), "Failed to load news", Toast.LENGTH_SHORT).show()
            }
        }
    }
}