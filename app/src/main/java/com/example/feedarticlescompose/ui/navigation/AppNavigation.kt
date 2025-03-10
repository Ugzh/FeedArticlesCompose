package com.example.feedarticlescompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feedarticlescompose.ui.screen.login.LoginScreen
import com.example.feedarticlescompose.ui.screen.login.LoginViewModel
import com.example.feedarticlescompose.ui.screen.register.RegisterScreen
import com.example.feedarticlescompose.ui.screen.register.RegisterViewModel

sealed class Screen(val route: String){
    data object Home: Screen("home")
    data object Register: Screen("register")
    data object Login: Screen("login")
    data object Edit: Screen("edit")
    data object Create: Screen("create")
    data object Splash: Screen("splash")
}

@Composable
fun AppNav(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ){
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
    }
}