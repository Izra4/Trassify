package com.praktikum.trassify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.praktikum.trassify.R
import com.praktikum.trassify.ui.components.AnimatedTextField
import com.praktikum.trassify.ui.components.LoadHeader
import com.praktikum.trassify.ui.theme.montserratFontFamily

@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit
) {
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LoadHeader(text = "Halo, Orang Lama!")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .statusBarsPadding()
                .padding(top = 250.dp, start = 40.dp, end = 40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Masuk",
                fontSize = 28.sp,
                color = Color.Black,
                fontFamily = montserratFontFamily,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedTextField(
                    field = "Email",
                    image = R.drawable.mail,
                    focusRequester = emailFocusRequester,
                    onNext = {passwordFocusRequester.requestFocus()}
                )

                Spacer(modifier = Modifier.height(12.dp))

                AnimatedTextField(
                    field = "Password",
                    image = R.drawable.lock,
                    focusRequester = passwordFocusRequester,
                    onDone = { keyboardController?.hide() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Handle regular login */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Masuk")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onGoogleSignInClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Google Logo",
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                                .padding(end = 8.dp)
                        )
                        Text(text = "Masuk dengan Google")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Belum punya akun?")
                    TextButton(onClick = {}) {
                        Text(text = "Daftar")
                    }
                }
            }
        }
    }
}