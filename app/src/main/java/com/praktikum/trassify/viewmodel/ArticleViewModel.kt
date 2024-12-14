package com.praktikum.trassify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.repository.ArticleRepository
import com.praktikum.trassify.data.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    private val _articleDetail = MutableStateFlow<Article?>(null)
    val articleDetail: StateFlow<Article?> = _articleDetail

    fun fetchArticleDetail(articleId: String) {
        viewModelScope.launch {
            repository.getArticleDetail(articleId).collectLatest { article ->
                _articleDetail.value = article
            }
        }
    }

    private val _articles = MutableStateFlow<Response<List<Article>>>(Response.Idle)
    val articles: StateFlow<Response<List<Article>>> = _articles

    // Fungsi untuk mengambil semua artikel
    fun getAllArticle() {
        viewModelScope.launch {
            _articles.value = Response.Loading
            val response = repository.getAllArticle()
            _articles.value = response
        }
    }
}

class ArticleViewModelFactory(
    private val repository: ArticleRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

