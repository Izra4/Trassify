package com.praktikum.trassify.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.praktikum.trassify.data.model.Article
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ArticleRepository {

    private val databaseRef = FirebaseDatabase.getInstance().getReference("articles")

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
            // Berhenti memantau ketika flow dibatalkan
            articleRef.removeEventListener(listener)
        }
    }
}
