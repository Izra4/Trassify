package com.praktikum.trassify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.trassify.composables.DropdownExample
import com.praktikum.trassify.composables.ScheduleCard
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White
import com.praktikum.trassify.viewmodel.ScheduleViewModel
import androidx.compose.ui.platform.LocalContext
import com.praktikum.trassify.composables.skeletons.MerchandiseSkeleton
import com.praktikum.trassify.composables.skeletons.ScheduleSkeleton
import com.praktikum.trassify.data.Response

@Composable()
fun ScheduleView(viewModel: ScheduleViewModel = viewModel(factory = ScheduleViewModel.Factory(LocalContext.current))){
    val schedulesState by viewModel.schedules.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllSchedules()
    }

    Box(
            modifier = Modifier.fillMaxSize() .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFA293FA),
                        Color(0xFF7C66FF)
                    ),
                    center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                    radius = 500f
                )
            ),

        ){
            Column {
                Spacer(modifier = Modifier.height(90.dp))
                Text(
                    text = "Jadwal Pemungutan Sampah",
                    style = TextType.text25SemiBold,
                    color = Color(0xFFDFDCF4),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(color = White)
                        .padding(20.dp)

                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        DropdownExample(
                            options = listOf("Sukun", "Sigura", "Testing"),
                            modifier = Modifier.weight(1f)
                        )
                        DropdownExample(
                            options = listOf("Sukun", "Sigura", "Testing"),
                            modifier = Modifier.weight(1f)
                        )
                        DropdownExample(
                            options = listOf("Sukun", "Sigura", "Testing"),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when(val state = schedulesState) {
                        is Response.Success -> {
                            LazyColumn (verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(state.data) { schedules ->
                                    ScheduleCard(
                                        schedule = schedules,
                                        modifier = Modifier.fillMaxWidth()
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
}

@Preview(showBackground = true)
@Composable()
fun ScheduleViewPreview(){
    ScheduleView()
}