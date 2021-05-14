package com.matvey.newsapp.viewModel

import androidx.lifecycle.*
import com.matvey.newsapp.Event
import com.matvey.newsapp.model.NewsRepository
import com.matvey.newsapp.model.database.News
import kotlinx.coroutines.*

class NewsListViewModel(private val repository: NewsRepository) : ViewModel() {

    val news: LiveData<List<News>> = repository.news.asLiveData()

    private val _showSnackBar = MutableLiveData<Event<String>>()
    val showSnackBar: LiveData<Event<String>>
        get() = _showSnackBar

    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun loadNews(query: String = "a") = viewModelScope.launch {
        isLoading.value = true
        val requestCode = repository.loadNewsWithRequestCode(query)
        if (requestCode == NewsRepository.NO_INTERNET_REQUEST_CODE) {
            _showSnackBar.value = Event(NO_INTERNET_MESSAGE)
        }
        isLoading.value = false
    }

    companion object {
        private const val NO_INTERNET_MESSAGE = "Нет подключения к сети"
    }
}

class NewsListViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}