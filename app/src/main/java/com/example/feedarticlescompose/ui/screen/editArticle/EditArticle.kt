package com.example.feedarticlescompose.ui.screen.editArticle

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.dtos.article.ArticleDto
import com.example.feedarticlescompose.ui.screen.sharedScreen.ArticleSharedScreenContent
import com.example.feedarticlescompose.utils.CategoryUtils

@Preview(showBackground = true)
@Composable
fun EditArticlePreview(){
    //EditArticleContent()
}

@Composable
fun EditArticleContent(
    article: ArticleDto,
    updateArticle: (String, String, String, Int) -> Unit
){
    val context = LocalContext.current
    var articleTitle by remember {
        mutableStateOf(article.titre)
    }
    var articleContent by remember {
        mutableStateOf(article.descriptif)
    }
    var articleUrl by remember {
        mutableStateOf(article.urlImage)
    }
    var categoryIdSelected =
        CategoryUtils.getCategoryByNumber(article.categorie)

    ArticleSharedScreenContent(
        titleScreen = context.getString(R.string.update),
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
        defaultValueSelected = categoryIdSelected,
        buttonTitle = context.getString(R.string.update),
        onClickButton = {
            updateArticle(
                articleTitle,
                articleContent,
                articleUrl,
                categoryIdSelected
            )
        }
    )
}

@Composable
fun EditArticleScreen(
    navController: NavController,
    vm: EditArticleViewModel,
    idArticle: Long
){
    val context = LocalContext.current
    val article by vm.articleStateFlow.collectAsState()

    vm.getArticle(idArticle)

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

    article?.let {
        EditArticleContent(it){ title, content, url, cat ->
            vm.updateArticle(title, content, url, cat)
        }
    }
}