package com.praktikum.trassify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.viewmodel.MerchandiseViewModel

@Composable
fun MerchandiseDetailScreen(
    merchId: String,
    viewModel: MerchandiseViewModel = viewModel()
) {
    val merchandiseDetail by viewModel.merchandiseDetail.collectAsState()

    // Fetch data once
    LaunchedEffect(merchId) {
        viewModel.fetchMerchDetail(merchId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECEFF1))
    ) {
        // Header
        Column(
            modifier = Modifier
                .background(Color(0xFF9575CD))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Merchandise",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Menambahkan pengecekan untuk Response.Idle
        when (val response = merchandiseDetail) {
            is Response.Loading -> {
                // Show loading spinner
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Response.Success -> {
                val merch = response.data

                // Highlighted Merchandise
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column {
                        // Image
                        AsyncImage(
                            model = merch.imageUrl,
                            contentDescription = "Merch Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${merch.points} Poin",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9575CD)
                                )
                                Text(
                                    text = "${merch.redeemedCount}x Ditukar",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = merch.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { /* TODO: Tukarkan sekarang */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9575CD)),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = "Tukarkan Sekarang",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Deskripsi
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Deskripsi",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = merch.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Reviews
                Text(
                    text = "Reviews",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(merch.reviews.values.toList()) { review ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                // Profile Image
                                AsyncImage(
                                    model = review.profileImageUrl,
                                    contentDescription = "User Profile",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(50))
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = review.username,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = review.comment,
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is Response.Error -> {
                // Show error message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = response.errorMessage ?: "Something went wrong", color = Color.Red)
                }
            }
            is Response.Idle -> {
                // Tampilkan UI Idle jika diperlukan
                // Misalnya, bisa memberi informasi atau menunggu pengambilan data
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Idle: Silakan Tunggu...", color = Color.Gray)
                }
            }
        }
    }
}

