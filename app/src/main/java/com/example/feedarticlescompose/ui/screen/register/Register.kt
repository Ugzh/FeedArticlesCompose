package com.example.feedarticlescompose.ui.screen.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.navigation.Screen
import com.example.feedarticlescompose.ui.sharedComponents.EditTextCustom
import com.example.feedarticlescompose.ui.sharedComponents.ScreenTitleCustom
import com.example.feedarticlescompose.ui.theme.BluePrimary

@Preview(showBackground = true)
@Composable
fun RegisterPreview(){
    RegisterContent(){_,_,_ ->}
}

@Composable
fun RegisterContent(registerUser : (String, String, String) -> Unit){
    val context = LocalContext.current

    var login by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }


    Box(
        contentAlignment =  Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ){
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            ScreenTitleCustom(
                title = context.getString(R.string.new_account)
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
                EditTextCustom(
                    value = confirmPassword,
                    label = context.getString(R.string.confirm_password),
                    isPasswordField = true
                ) {
                    confirmPassword = it
                }
            }
            Button(
                onClick = { registerUser(login, password, confirmPassword) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary
                )
            ) {
                Text(text = context.getString(R.string.register))
            }
            Spacer(modifier = Modifier.size(1.dp))
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController, vm: RegisterViewModel){
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        vm.userMessageSharedFlow.collect{
            Toast.makeText(context, context.getString(it), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = true) {
        vm.triggerNavigationToLogin.collect{
            if(it)
                navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Login.route){
                        inclusive = true
                    }
                }
        }
    }
    RegisterContent{ login, password, confirmPassword ->
        vm.registerUser(login, password, confirmPassword)
    }
}