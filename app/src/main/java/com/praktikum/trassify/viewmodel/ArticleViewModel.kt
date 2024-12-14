package com.praktikum.trassify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}
