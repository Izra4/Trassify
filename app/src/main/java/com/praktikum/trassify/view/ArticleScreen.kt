package com.praktikum.trassify.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.praktikum.trassify.R
import com.praktikum.trassify.viewmodel.ArticleDetailViewModel

@Composable
fun ArticleDetailScreen(
    articleId: String,
    viewModel: ArticleDetailViewModel = viewModel()
) {
    val article by viewModel.articleDetail.collectAsState()

    // Fetch Article Data
    viewModel.fetchArticleDetail(articleId)

    // Scrollable Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECEFF1))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .background(Color(0xFF6650a4))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Artikel",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = montserratFont,
                color = Color.White
            )
        }

        article?.let { articleData ->
            // Featured Image
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(articleData.imageUrl),
                    contentDescription = "Article Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            // Text Tanggal
            Text(
                text = articleData.date,
                fontSize = 12.sp,
                fontFamily = montserratFont,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.End)
            )

            // Title and Content
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = articleData.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = montserratFont
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = articleData.content,
                    fontSize = 16.sp,
                    fontFamily = montserratFont,
                    color = Color.Gray
                )
            }
        } ?: run {
            // Loading State
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
fun ArticleDetailScreenPreview() {
    ArticleDetailScreen(articleId = "example-article-id")
}