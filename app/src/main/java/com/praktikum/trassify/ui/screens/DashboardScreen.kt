package com.praktikum.trassify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.praktikum.trassify.data.model.User
import com.praktikum.trassify.ui.theme.TextType

@Composable
fun DashboardScreen(
    user: User?,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Selamat datang,",
            style = TextType.text24Md
        )

        Text(
            text = user?.name ?: "User",
            style = TextType.text24Md,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onSignOutClick,
            modifier = Modifier.width(200.dp)
        ) {
            Text(text = "Keluar")
        }
    }
}