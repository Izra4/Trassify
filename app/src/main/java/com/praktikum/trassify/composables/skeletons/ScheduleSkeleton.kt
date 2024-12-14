package com.praktikum.trassify.composables.skeletons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.composables.Skeleton

@Composable()
fun ScheduleSkeleton(){
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(65.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Skeleton(modifier = Modifier.fillMaxSize())
    }

    Column (
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Skeleton(modifier = Modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(16.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Skeleton(modifier = Modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .align(Alignment.End)
                .height(16.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Skeleton(modifier = Modifier.fillMaxSize())
        }
    }
}
