package com.praktikum.trassify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.trassify.model.Article
import com.praktikum.trassify.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel : ViewModel() {
    private val repository = ArticleRepository()

    private val _article = MutableStateFlow<Article?>(null)
    val article = _article.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun fetchArticleById(articleId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getArticleById(articleId)
                    .collect { fetchedArticle ->
                        _article.value = fetchedArticle
                        _error.value = null
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "Gagal memuat artikel"
                _article.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}