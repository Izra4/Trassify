package com.praktikum.trassify.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.praktikum.trassify.MainActivity
import com.praktikum.trassify.R
import com.praktikum.trassify.ui.theme.TrassifyTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            TrassifyTheme {
                SplashScreen()
            }
        }
    }
}

@Composable()
fun SplashScreen(){
    val context = LocalContext.current
    LaunchedEffect(true
    ){
        delay(2000)
        context.startActivity(Intent(context, MainActivity::class.java))
        if(context is SplashActivity){
            context.finish()
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(R.drawable.logo_splash),
            contentDescription = "splash logo",
            modifier = Modifier.width(300.dp).height(107.dp))
    }
}