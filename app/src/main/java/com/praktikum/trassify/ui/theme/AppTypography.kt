package com.praktikum.trassify.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.praktikum.trassify.R

object AppTypography{
    val h1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )
    val text = TextStyle(
        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal
    )
}