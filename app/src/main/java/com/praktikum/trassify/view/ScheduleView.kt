package com.praktikum.trassify.view

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.praktikum.trassify.composables.DropdownExample
import com.praktikum.trassify.composables.ScheduleCard
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White
import com.praktikum.trassify.viewmodel.ScheduleViewModel
import androidx.compose.ui.platform.LocalContext
import com.praktikum.trassify.composables.skeletons.ScheduleSkeleton
import com.praktikum.trassify.data.Response
import com.praktikum.trassify.utils.extractDateAndTime

@Composable
fun ScheduleView(viewModel: ScheduleViewModel) {
    val schedulesState by viewModel.schedules.collectAsState()
    val villagesState by viewModel.villages.collectAsState()
    val subdistrictsState by viewModel.subdistricts.collectAsState()
    val timesState by viewModel.times.collectAsState()

    val selectedVillage = remember { mutableStateOf("") }
    val selectedSubdistrict = remember { mutableStateOf("") }
    val selectedTime = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAllSchedules()
    }

    LaunchedEffect(selectedVillage.value, selectedSubdistrict.value, selectedTime.value) {
        viewModel.filterSchedules(
            selectedVillage.value,
            selectedSubdistrict.value,
            selectedTime.value
        )
    }

    val formattedTimes = timesState.map { extractDateAndTime(it).second }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.radialGradient(
                colors = listOf(Color(0xFFA293FA), Color(0xFF7C66FF)),
                center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                radius = 500f
            )
        )
    ) {
        Column {
            Spacer(modifier = Modifier.height(90.dp))
            Text(
                text = "Jadwal Pemungutan Sampah",
                style = TextType.text25SemiBold,
                color = Color(0xFFDFDCF4),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = White)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    DropdownExample(
                        options = villagesState,
                        modifier = Modifier.weight(1f),
                        selectedOption = selectedVillage.value,
                        onOptionSelected = { selectedVillage.value = it }
                    )
                    DropdownExample(
                        options = subdistrictsState,
                        modifier = Modifier.weight(1f),
                        selectedOption = selectedSubdistrict.value,
                        onOptionSelected = { selectedSubdistrict.value = it }
                    )
                    DropdownExample(
                        options = formattedTimes,
                        modifier = Modifier.weight(1f),
                        selectedOption = selectedTime.value,
                        onOptionSelected = { selectedTime.value = it }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                when (val state = schedulesState) {
                    is Response.Success -> {
                        Log.d("data schedule", state.data.toString())
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(state.data) { schedule ->
                                ScheduleCard(
                                    schedule = schedule,
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
                    is Response.Idle -> {}
                }
            }
        }
    }
}
