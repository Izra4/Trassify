package com.praktikum.trassify.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.viewmodel.ArticleViewModel

@Composable
fun ArticleDetailScreen(
    articleId: String,
    viewModel: ArticleViewModel = viewModel()
) {
    val article by viewModel.articleDetail.collectAsState(initial = null)

    // Fetch Data
    LaunchedEffect(articleId) {
        viewModel.fetchArticleDetail(articleId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6650a4))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Artikel",
                style = TextType.text25SemiBold,
                color = Color.White
            )
        }

        // Konten
        if (article == null) {
            // Loading State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val articleData = article!!
            // Card berisi gambar + tanggal
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = rememberAsyncImagePainter(articleData.imageUrl),
                        contentDescription = "Article Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    // Tanggal di pojok kanan atas
                    Text(
                        text = articleData.timestamp,
                        style = TextType.text12Rg,
                        color = Color.White,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .background(
                                color = Color(0x80000000),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Judul
            Text(
                text = articleData.title,
                style = TextType.text24Md,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Konten
            Text(
                text = articleData.content,
                style = TextType.text13Rg,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun ArticleDetailScreenPreview() {
    ArticleDetailScreen(articleId = "example-id")
}
