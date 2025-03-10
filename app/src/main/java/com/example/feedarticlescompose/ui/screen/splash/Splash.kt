package com.example.feedarticlescompose.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.navigation.Screen
import com.example.feedarticlescompose.ui.theme.BluePrimary

@Preview(showBackground = true)
@Composable
fun SplashPreview(){
    SplashContent()
}
@Composable
fun SplashContent(){
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(BluePrimary)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = R.drawable.feedarticles_logo,
            contentDescription = context.getString(R.string.app_title)
        )
        Text(
            text = context.getString(R.string.app_title),
            color = Color.White,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun SplashScreen(navController: NavController, vm: SplashViewModel){
    LaunchedEffect(key1 = true) {
        vm.userAlreadyConnectedSharedFlow.collect{
            if(it)
                navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Splash.route){
                        inclusive = true
                    }
                }
            else
                navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Splash.route){
                        inclusive = true
                    }
                }
        }
    }
    SplashContent()
}