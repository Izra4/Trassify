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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.composables.Skeleton
import com.praktikum.trassify.ui.theme.White

@Composable
fun ProfileSkeleton() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(White)

        ) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)
            ) {

                Skeleton(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)

                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(20.dp)
                            .clip(RoundedCornerShape(8.dp))

                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(8.dp))

                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        Skeleton(
                            modifier = Modifier
                                .width(80.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))

                        )

                        Spacer(modifier = Modifier.width(8.dp))


                        Skeleton(
                            modifier = Modifier
                                .width(80.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))

                        )
                    }
                }
            }
        }
    }
}
