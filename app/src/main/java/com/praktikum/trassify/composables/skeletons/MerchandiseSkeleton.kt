package com.praktikum.trassify.composables.skeletons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.composables.Skeleton
import com.praktikum.trassify.ui.theme.White

@Composable
fun MerchandiseSkeleton() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Skeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))


            Skeleton(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Skeleton(
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                )


                Skeleton(
                    modifier = Modifier
                        .width(50.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}
