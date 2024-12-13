package com.praktikum.trassify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.navigation.NavController
import com.praktikum.trassify.R
import com.praktikum.trassify.ui.components.AnimatedTextField
import com.praktikum.trassify.ui.components.LoadHeader
import com.praktikum.trassify.ui.theme.MainPurple
import com.praktikum.trassify.ui.theme.TextType

@Composable
fun LoginScreen(
    navController: NavController,
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
                color = Color.Black,
                letterSpacing = 3.sp,
                style = TextType.text25SemiBold,
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

                Spacer(modifier = Modifier.height(26.dp))

                AnimatedTextField(
                    field = "Password",
                    image = R.drawable.lock,
                    focusRequester = passwordFocusRequester,
                    onDone = { keyboardController?.hide() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column (
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 42.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { /* Handle regular login */ },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainPurple
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = "Masuk",
                                style = TextType.text16SemiBold,
                                color = Color.White,
                                modifier = Modifier.padding(15.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 1.dp,
                                color = Color.Gray
                            )

                            Text(
                                text = "Atau",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 8.dp) // Menambahkan padding horizontal
                            )

                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 1.dp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = onGoogleSignInClick,
                            modifier = Modifier
                                .border(1.dp, Color.Gray, RoundedCornerShape(24.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                        ) {
                            Row(
                                modifier = Modifier.padding(4.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.search),
                                    contentDescription = "Google Logo",
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(24.dp)
                                        .padding(end = 12.dp)
                                )
                                Text(text = "Masuk dengan Google")
                            }
                        }

                        Spacer(modifier = Modifier.height(52.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Belum punya akun?",
                                style = TextType.text13Rg
                            )
                            TextButton(onClick = {navController.navigate("register")}) {
                                Text(
                                    text = "Daftar",
                                    color = MainPurple,
                                    style = TextType.text13Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}