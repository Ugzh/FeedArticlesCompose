package com.example.feedarticlescompose.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.navigation.Screen
import com.example.feedarticlescompose.ui.screen.register.RegisterContent
import com.example.feedarticlescompose.ui.sharedComponents.EditTextCustom
import com.example.feedarticlescompose.ui.sharedComponents.ScreenTitleCustom
import com.example.feedarticlescompose.ui.theme.BluePrimary

@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    LoginContent(goToRegisterScreen = {}){ _,_ ->
    }
}

@Composable
fun LoginContent(
    goToRegisterScreen : () -> Unit,
    loginUser: (String, String)-> Unit
){
    val context = LocalContext.current
    var login by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        contentAlignment =  Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .systemBarsPadding()
        ) {
            ScreenTitleCustom(
                title = context.getString(R.string.sign_in)
            )

            Column {
                EditTextCustom(
                    value = login,
                    label = context.getString(R.string.login),
                ) {
                    login = it
                }
                EditTextCustom(
                    value = password,
                    label = context.getString(R.string.password),
                    isPasswordField = true

                ) {
                    password = it
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { loginUser(login, password) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePrimary
                    )
                    ) {
                    Text(text = context.getString(R.string.connect))
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = context.getString(R.string.no_account_create_here),
                    color = BluePrimary,
                    modifier = Modifier.clickable {
                        goToRegisterScreen()
                    }
                )
            }
            Spacer(modifier = Modifier.size(1.dp))
        }
    }
}

@Composable
fun LoginScreen(navController: NavController, vm: LoginViewModel){
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        vm.userMessageSharedFlow.collect{
            Toast.makeText(context, context.getString(it), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = true) {
        vm.isLoggedSharedFlow.collect{
            if(it)
                navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Login.route){
                        inclusive = true
                    }
                }
        }
    }

    LoginContent(goToRegisterScreen = {
        navController.navigate(Screen.Register.route)
    }) { login, password ->
        vm.logUser(login,password)
    }
}