package com.praktikum.trassify.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.praktikum.trassify.R
import com.praktikum.trassify.ui.theme.TextType

@Composable
fun LoadHeader(text: String) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.header),
            contentDescription = "Header",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = text,
            style = TextType.text49SemiBold,
            lineHeight = 72.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(45.dp, 65.dp, 0.dp, 0.dp)
        )
    }
}