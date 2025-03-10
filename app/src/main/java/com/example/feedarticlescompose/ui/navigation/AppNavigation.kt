package com.example.feedarticlescompose.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feedarticlescompose.ui.screen.addArticle.AddArticleScreen
import com.example.feedarticlescompose.ui.screen.addArticle.AddArticleViewModel
import com.example.feedarticlescompose.ui.screen.editArticle.EditArticleContent
import com.example.feedarticlescompose.ui.screen.editArticle.EditArticleScreen
import com.example.feedarticlescompose.ui.screen.editArticle.EditArticleViewModel
import com.example.feedarticlescompose.ui.screen.home.HomeScreen
import com.example.feedarticlescompose.ui.screen.home.HomeViewModel
import com.example.feedarticlescompose.ui.screen.login.LoginScreen
import com.example.feedarticlescompose.ui.screen.login.LoginViewModel
import com.example.feedarticlescompose.ui.screen.register.RegisterScreen
import com.example.feedarticlescompose.ui.screen.register.RegisterViewModel
import com.example.feedarticlescompose.ui.screen.splash.SplashScreen
import com.example.feedarticlescompose.ui.screen.splash.SplashViewModel

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
        startDestination = Screen.Splash.route
    ){
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController, homeViewModel)
        }
        composable(Screen.Create.route) {
            val addArticleViewModel: AddArticleViewModel = hiltViewModel()
            AddArticleScreen(navController, addArticleViewModel)
        }
        composable(Screen.Edit.route+"/{idArticle}",
            arguments = listOf(navArgument(name= "idArticle"){
                type = NavType.LongType
            })
        ) {
            Log.d("ici", "ici")
            it.arguments?.getLong("idArticle")?.let {idArticle ->
                 Log.d("ici", idArticle.toString())
                val editArticleViewModel: EditArticleViewModel = hiltViewModel()
                EditArticleScreen(navController, editArticleViewModel, idArticle)
            }
        }
    }
}