package com.praktikum.trassify.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.R
import com.praktikum.trassify.composables.ArticleCard
import com.praktikum.trassify.composables.MerchandiseCard
import com.praktikum.trassify.composables.ProfileCard
import com.praktikum.trassify.composables.ScheduleCard
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.viewmodel.DashboardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.praktikum.trassify.composables.Skeleton
import com.praktikum.trassify.composables.skeletons.ArticleSkeleton
import com.praktikum.trassify.composables.skeletons.MerchandiseSkeleton
import com.praktikum.trassify.composables.skeletons.ProfileSkeleton
import com.praktikum.trassify.composables.skeletons.ScheduleSkeleton
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.viewmodel.DashboardViewModelFactory

@Composable()
fun DashboardView(
    viewModel: DashboardViewModel,
    navController: NavController) {
    val profileState by viewModel.userProfile.collectAsState()
    val merchandisesState by viewModel.merchandises.collectAsState()
    val articlesState by viewModel.articles.collectAsState()
    val schedulesState by viewModel.schedules.collectAsState()

    // Mendapatkan userId dari FirebaseAuth
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    LaunchedEffect(Unit) {
        viewModel.userProfile(userId!!)
        viewModel.getAllMerchandise()
        viewModel.getAllArticle()
        viewModel.getAllSchedules()
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Box{
            Image(
                painter = painterResource(id = R.drawable.circle_dashboard),
                contentDescription = "circle",
                modifier =Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 40.dp, end = 20.dp)
            ) {
                when (val state = profileState) {
                    is Response.Loading -> {
                        Skeleton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                        )
                    }
                    is Response.Success -> {
                        val user = state.data
                        Text(text = "Selamat Siang, ${user?.name}!", style = TextType.text25SemiBold, color = Color(0xFFDFDCF4))
                    }
                    is Response.Error -> {
                        Text(
                            text = state.errorMessage,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is Response.Idle -> {

                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                when (val state = profileState) {
                    is Response.Loading -> {
                       ProfileSkeleton()
                    }
                    is Response.Success -> {
                        val user = state.data
                        ProfileCard(
                            username = user?.name ?: "Guest",
                            point = user?.point ?: 0,
                            image = user?.imageUrl ?: ""
                        )
                    }
                    is Response.Error -> {
                        Text(
                            text = state.errorMessage,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is Response.Idle -> {
                    }
                }

            }
        }
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Artikel Terkini",
                style =  TextType.text15SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            when(val state = articlesState) {
                is Response.Success -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.data) { article ->
                            ArticleCard(
                                title = article.title,
                                image = article.imageUrl,
                                content = article.content,
                                onClick = {
                                    navController.navigate("article/${article.id}")
                                }
                            )
                        }
                    }
                }
                is Response.Loading -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(5) {
                            ArticleSkeleton()
                        }
                    }
                }
                is Response.Error -> {
                    Text(
                        text = state.errorMessage,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is Response.Idle -> {

                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Merchandise Populer",
                style =  TextType.text15SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            when(val state = merchandisesState) {
                is Response.Success -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.data) { merchandise ->
                            MerchandiseCard(
                                title = merchandise.name,
                                image = merchandise.imageUrl,
                                point = merchandise.points,
                                exchange = merchandise.redeemedCount,
                                onClick = {
                                    navController.navigate("merchandise/${merchandise.id}")
                                }
                            )
                        }
                    }
                }
                is Response.Loading -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(5) {
                            MerchandiseSkeleton()
                        }
                    }
                }
                is Response.Error -> {
                    Text(
                        text = state.errorMessage,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is Response.Idle -> {

                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Jadwal Pungut Sampah Kamu",
                style =  TextType.text15SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            when(val state = schedulesState) {
                is Response.Success -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.data) { schedules ->
                            ScheduleCard(
                                schedule = schedules
                            )
                        }
                    }
                }
                is Response.Loading -> {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(5) {
                            ScheduleSkeleton()
                        }
                    }
                }
                is Response.Error -> {
                    Text(
                        text = state.errorMessage,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is Response.Idle -> {

                }
            }


        }
    }
}
