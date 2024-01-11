package com.example.thejedijournal

import android.annotation.*
import android.content.*
import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import com.example.thejedijournal.ui.*
import com.example.thejedijournal.ui.theme.*
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheJediJournalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen {
                        startActivity(Intent(this@SplashScreenActivity, CharacterListHomeScreen::class.java))
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navigateToMainScreen: () -> Unit) {

    LaunchedEffect(key1 = true, block = {
        delay(2000)
        navigateToMainScreen()
    })
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center) {

        Image(painter = painterResource(
            id = R.drawable.background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds)

        Image(painter = painterResource(
            id = R.drawable.star_wars),
            contentDescription = "Star wars logo")
    }
}