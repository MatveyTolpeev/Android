package com.matvey.newsapp.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class News(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Int,

    @ColumnInfo(name="title")
    val title: String,

    @ColumnInfo(name="author")
    val author: String,

    @ColumnInfo(name="content")
    val content: String,

    @ColumnInfo(name="url")
    val url: String,

    @ColumnInfo(name="urlToImage")
    val urlToImage: String,


)