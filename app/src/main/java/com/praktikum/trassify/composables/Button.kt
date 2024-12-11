package com.praktikum.trassify.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.ui.theme.Primary10
import com.praktikum.trassify.ui.theme.TextType
import com.praktikum.trassify.ui.theme.White

@Composable()
fun MiniButton(
    onClick : () -> Unit,
    label : String
){
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Primary10)
            .border(shape = RoundedCornerShape(50), width = 0.dp, color = Primary10)
            .clickable (
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 5.dp)
    ){
        Text(text = label, style = TextType.text9Md, color = White)
    }
}
