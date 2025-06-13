package com.example.foodmitra

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodmitradonar.R
import com.example.foodmitradonar.SharedPref.SharedPrefHelper
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    var startAnim by remember { mutableStateOf(false) }


    val sharedPref = remember { SharedPrefHelper(context) }
    val isLoggedIn = sharedPref.isDonorRegistered()


    // Start animation & navigate after delay
    LaunchedEffect(Unit) {
        startAnim = true
        delay(2500)
        navController.navigate(if (isLoggedIn) "bottom_nav" else "login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.7f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "scaleAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.donar),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp).scale(scaleAnim)
            )

            AnimatedVisibility(visible = startAnim, enter = fadeIn(tween(1200))) {
                Text(
                    text = "Ann ka sahi daan, har pet ka samman",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
