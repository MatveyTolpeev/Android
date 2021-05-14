package com.matvey.newsapp.model

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.matvey.newsapp.managers.NetManager
import com.matvey.newsapp.model.database.News
import com.matvey.newsapp.model.database.NewsDao
import com.matvey.newsapp.network.APIService
import java.net.ConnectException
import java.net.UnknownHostException

class NewsRepository(private val context: Context, private val newsDao: NewsDao) {

    val news = newsDao.getAllNews()

    suspend fun clearNews() = newsDao.deleteAll()

    suspend fun getNewById(id: Int) = newsDao.getNewById(id)

    suspend fun loadNewsWithRequestCode(query: String) : Int {
        val netManager = NetManager(context)
        netManager.isConnectedToInternet?.let {
             if (it) {
                 clearNews()
                 return try {
                     getNews(query=query).also { news ->
                         saveNews(news)
                     }
                     SUCCESS_REQUEST_CODE
                 } catch (e: ConnectException) {
                     NO_INTERNET_REQUEST_CODE
                 } catch (e: UnknownHostException) {
                     NO_INTERNET_REQUEST_CODE
                 }
            }
        }
        return NO_INTERNET_REQUEST_CODE
    }

    fun loadImage(url: String, view: ImageView) {
        Glide
            .with(context)
            .load(url)
            .into(view)
    }

    private suspend fun getNews(query: String): List<News> {

        val apiService = APIService.create()
        val result = apiService.getNews(query = query).articles
        return result
    }

    private suspend fun saveNews(news: List<News>) {
        for (new in news) {
            newsDao.insert(new)
        }
    }

    companion object {
        const val SUCCESS_REQUEST_CODE = 0
        const val NO_INTERNET_REQUEST_CODE = 1

    }
}