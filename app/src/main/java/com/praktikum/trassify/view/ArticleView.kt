package com.praktikum.trassify.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.praktikum.trassify.R
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.data.model.Article
import com.praktikum.trassify.viewmodel.ArticleViewModel

@Composable
fun ArticleScreen(
    navController: NavController,
    viewModel: ArticleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Observasi state artikel dari ViewModel
    val articlesState = viewModel.articles.collectAsState()

    // Memanggil fungsi getAllArticle() ketika composable pertama kali dibangun
    LaunchedEffect(Unit) {
        viewModel.getAllArticle()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Cari Artikel") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // List of Articles berdasarkan state
        when (val result = articlesState.value) {
            is Response.Success -> {
                // Jika artikel berhasil di-load, tampilkan daftar artikel
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(result.data) { article ->
                        ArticleItem(article, navController)
                    }
                }
            }
            is Response.Loading -> {
                // Tampilkan indikator loading jika data sedang dimuat
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Response.Error -> {
                // Tampilkan pesan error jika terjadi kesalahan saat mengambil data
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${result.errorMessage ?: "Terjadi kesalahan"}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                // State Idle atau tidak ada data
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No data available")
                }
            }
        }
    }
}

@Composable
fun ArticleItem(article: Article, navController: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { // Add clickable behavior
                // Navigate to article detail screen
                navController.navigate("article/${article.id}")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                val imagePainter = if (article.imageUrl.isNullOrEmpty()) {
                    painterResource(id = R.drawable.article_image)
                } else {
                    rememberAsyncImagePainter(model = article.imageUrl)
                }
                Image(
                    painter = imagePainter,
                    contentDescription = "Article Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = article.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = article.content,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
            }
            Text(
                text = article.timestamp,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

