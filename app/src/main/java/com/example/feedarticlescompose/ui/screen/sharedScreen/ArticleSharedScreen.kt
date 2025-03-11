package com.example.feedarticlescompose.ui.screen.sharedScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.sharedComponents.CategoryContent
import com.example.feedarticlescompose.ui.sharedComponents.EditTextCustom
import com.example.feedarticlescompose.ui.sharedComponents.ScreenTitleCustom
import com.example.feedarticlescompose.ui.theme.BluePrimary

@Composable
fun ArticleSharedScreenContent(
    titleScreen: String,
    articleTitle: String,
    articleTitleLabel: String,
    onArticleTitleChange: (String) -> Unit,
    articleContent: String = "",
    articleContentLabel: String,
    onArticleContentChange: (String) -> Unit,
    articleUrl: String = "",
    articleUrlLabel: String,
    onArticleUrlChange: (String) -> Unit,
    radioOptions: List<Int>,
    getIdSelect: (Int) -> Unit,
    defaultValueSelected: Int = R.string.all,
    buttonTitle: String,
    onClickButton : () -> Unit
){
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight()) {
            ScreenTitleCustom(title = titleScreen)
            Spacer(modifier = Modifier.size(20.dp))
            EditTextCustom(
                value = articleTitle,
                label = articleTitleLabel,
                onValueChange = onArticleTitleChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))
            EditTextCustom(
                value = articleContent,
                label = articleContentLabel,
                onValueChange = onArticleContentChange,
                modifier = Modifier.height(140.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))
            EditTextCustom(
                value = articleUrl,
                label = articleUrlLabel,
                onValueChange = onArticleUrlChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))
            AsyncImage(
                model = articleUrl,
                error = painterResource(id = R.drawable.feedarticles_logo),
                contentDescription = context.getString(R.string.image_of, articleTitle),
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))
            CategoryContent(
                radioOptions = radioOptions,
                defaultValueSelected,
                getIdSelected = getIdSelect
            )
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                onClick = onClickButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary
                )
            ) {
                Text(text = buttonTitle)
            }
        }
    }
}