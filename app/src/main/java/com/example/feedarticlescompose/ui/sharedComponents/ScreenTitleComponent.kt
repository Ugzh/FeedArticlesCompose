package com.example.feedarticlescompose.ui.sharedComponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedarticlescompose.ui.theme.BluePrimary

@Preview(showBackground = true)
@Composable
fun ScreenTitleCustomPreview(){
    ScreenTitleCustom("New Account")
}

@Composable
fun ScreenTitleCustom(
    title: String,
    modifier: Modifier = Modifier
){
    Text(
        text = title,
        color = BluePrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(vertical = 10.dp)
    )
}