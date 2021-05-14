package com.matvey.newsapp.viewModel

import android.widget.ImageView
import androidx.lifecycle.*
import com.matvey.newsapp.model.NewsRepository
import com.matvey.newsapp.model.database.News
import kotlinx.coroutines.launch

class NewDetailsViewModel(private val repository: NewsRepository) : ViewModel() {
    val new = MutableLiveData<News?>()

    fun loadNewById(id: Int) = viewModelScope.launch {
        new.value = repository.getNewById(id)
    }

    fun loadImage(url: String, imageView: ImageView) {
        repository.loadImage(url, imageView)
    }
}

class NewDetailsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}