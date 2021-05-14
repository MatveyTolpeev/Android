package com.matvey.newsapp

import android.app.Application
import com.matvey.newsapp.model.NewsRepository
import com.matvey.newsapp.model.database.NewsRoomDatabase

class NewsApplication : Application() {
    val database by lazy { NewsRoomDatabase.getDatabase(this) }
    val repository by lazy { NewsRepository(this, database.newsDao()) }
}