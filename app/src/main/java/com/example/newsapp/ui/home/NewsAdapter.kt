package com.example.newsapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newsapp.R
import com.example.newsapp.WebViewActivity
import com.example.newsapp.data.Article

class NewsAdapter(private var articleList: List<Article?>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // ViewHolder to hold the views for each item
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.articleImage)
        val articleTitle: TextView = itemView.findViewById(R.id.articleTitle)
        val articleDescription: TextView = itemView.findViewById(R.id.articleDescription)
        val articleAuthor: TextView = itemView.findViewById(R.id.articleAuthor)
        val articlePublishedAt: TextView = itemView.findViewById(R.id.articlePublishedAt)
        val articleSource: TextView = itemView.findViewById(R.id.source)
    }

    // Inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return NewsViewHolder(view)
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articleList[position]

        holder.articleTitle.text = article?.title
        holder.articleDescription.text = article?.description
        holder.articleAuthor.text = article?.author ?: "Unknown"
        holder.articlePublishedAt.text = article?.publishedAt
        holder.articleSource.text = article?.source?.name ?: "Unknown Source"

        // Handle link click to open in WebViewActivity
        holder.articleSource.setOnClickListener {
            val context = holder.itemView.context
            val url = article?.url ?: return@setOnClickListener
            val intent = Intent(context, WebViewActivity::class.java).apply {
                putExtra("url", url)
            }
            context.startActivity(intent)
        }

        // Use Coil to load images
        holder.articleImage.load(article?.urlToImage) {
            crossfade(true)
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.error_image)
        }
    }

    // Return the size of the list
    override fun getItemCount(): Int {
        return articleList.size
    }

    // Update the article list dynamically
    fun updateArticles(newArticles: List<Article?>?) {
        if (newArticles != null) {
            articleList = newArticles
            notifyDataSetChanged()
        }
    }
}
