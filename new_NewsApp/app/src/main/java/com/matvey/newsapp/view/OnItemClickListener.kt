package com.matvey.newsapp.view

import com.matvey.newsapp.model.database.News

interface OnItemClickListener {
    fun onItemClicked(contact: News)
}