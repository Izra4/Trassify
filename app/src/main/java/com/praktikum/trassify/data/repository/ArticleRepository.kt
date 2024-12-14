package com.praktikum.trassify.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Article
import com.praktikum.trassify.data.remote.FirebaseRemote
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ArticleRepository(
    private val database: DatabaseReference
) {

    // Di sini, kita asumsikan 'database' sudah mengarah ke root atau node tertentu.
    // Untuk mengambil data artikel, kita arahkan ke child "articles".
    private val databaseRef = database.child("articles")

    fun getArticleDetail(articleId: String): Flow<Article?> = callbackFlow {
        val articleRef = databaseRef.child(articleId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val article = snapshot.getValue(Article::class.java)
                trySend(article).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        articleRef.addValueEventListener(listener)

        awaitClose {
            articleRef.removeEventListener(listener)
        }
    }

    suspend fun getAllArticle(): Response<List<Article>> {
        return try {
            val articles = FirebaseRemote.getAllData<Article>("articles")
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
