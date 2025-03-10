package com.example.feedarticlescompose.ui.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.feedarticlescompose.ui.theme.BluePrimary

@Preview(showBackground = true)
@Composable
fun EditTextCustomPreview(){
    var text by remember {
        mutableStateOf("")
    }
    EditTextCustom(text, "Test", ){text = it}
}

@Composable
fun EditTextCustom(
    value: String,
    label: String = "",
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPasswordField: Boolean = false,
    onValueChange: (String)-> Unit,
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
                Text(text = label, color = BluePrimary)
        },
        colors = TextFieldDefaults
            .colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = BluePrimary,
                unfocusedIndicatorColor = BluePrimary
                ),
        textStyle = TextStyle(color = BluePrimary),
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation =
        if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None
    )
}