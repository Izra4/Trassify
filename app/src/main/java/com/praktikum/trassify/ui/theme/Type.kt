package com.praktikum.trassify.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.praktikum.trassify.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val montserratFontFamily = FontFamily(
    Font(R.font.mont_md, FontWeight.Medium),
    Font(R.font.mont_rg, FontWeight.Normal),
    Font(R.font.mont_bold, FontWeight.Bold),
    Font(R.font.mont_thin, FontWeight.Thin),
    Font(R.font.mont_black, FontWeight.Black),
    Font(R.font.mont_light, FontWeight.Light),
    Font(R.font.mont_extra_bold, FontWeight.ExtraBold),
    Font(R.font.mont_extra_light, FontWeight.ExtraLight),
    Font(R.font.mont_semi_bold, FontWeight.SemiBold)
)