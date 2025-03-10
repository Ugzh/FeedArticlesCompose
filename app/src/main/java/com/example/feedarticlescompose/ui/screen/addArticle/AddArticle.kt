package com.example.feedarticlescompose.ui.screen.addArticle

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.ui.navigation.Screen
import com.example.feedarticlescompose.ui.screen.sharedScreen.ArticleSharedScreenContent
import com.example.feedarticlescompose.utils.CategoryUtils

@Preview(showBackground = true)
@Composable
fun AddArticlePreview() {
    AddArticleContent(
        {_,_,_,_ ->}
    )
}

@Composable
fun AddArticleContent(
    createArticle: (String, String, String, Int) -> Unit
) {
    val context = LocalContext.current
    var articleTitle by remember {
        mutableStateOf("")
    }
    var articleContent by remember {
        mutableStateOf("")
    }
    var articleUrl by remember {
        mutableStateOf("")
    }
    var categoryIdSelected = R.string.sport

    ArticleSharedScreenContent(
        titleScreen = context.getString(R.string.new_article),
        articleTitle = articleTitle,
        articleTitleLabel = context.getString(R.string.title),
        onArticleTitleChange = {
            if (it.length <= 80)
                articleTitle = it
        },
        articleContent = articleContent,
        articleContentLabel = context.getString(R.string.content),
        onArticleContentChange = {
            articleContent = it
        },
        articleUrl = articleUrl,
        articleUrlLabel = context.getString(R.string.image_url),
        onArticleUrlChange = {
            articleUrl = it
        },
        radioOptions = CategoryUtils.listOfCategories,
        getIdSelect = {
            categoryIdSelected = it
        },
        buttonTitle = context.getString(R.string.register),
        onClickButton = {
            createArticle(
                articleTitle,
                articleContent,
                articleUrl,
                categoryIdSelected
            )
        }
    )
}

@Composable
fun AddArticleScreen(navController: NavController, vm: AddArticleViewModel) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        vm.userMessageSharedFlow.collect{
            Toast.makeText(context, context.getString(it), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = true) {
        vm.triggerNavigationToHomeSharedFlow.collect{
            if(it)
                navController.popBackStack()
        }
    }

    AddArticleContent{ title, content, url, cat ->
        vm.createArticle(
            title,
            content,
            url,
            cat
        )
    }
}