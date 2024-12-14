package com.praktikum.trassify.data.repository



import android.util.Log
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Article
import com.praktikum.trassify.data.remote.FirestoreRemote

class ArticleRepository{
    suspend fun getAllArticle(): Response<List<Article>> {
        return try {
            val articles = FirestoreRemote.getAllData<Article>("articles")
            if (articles.isEmpty()) {
                Response.Error("article is empty")
            } else {
                Response.Success(articles)

            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "An unknown error occurred")
        }
    }

}