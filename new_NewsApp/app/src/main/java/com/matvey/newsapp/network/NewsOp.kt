package com.matvey.newsapp.network

import com.matvey.newsapp.model.database.News

data class NewsOp(
    val totalResults: String,
    val articles: List<News>

)