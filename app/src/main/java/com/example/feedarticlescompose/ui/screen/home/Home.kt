package com.example.feedarticlescompose.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.dtos.article.ArticleDto
import com.example.feedarticlescompose.ui.navigation.Screen
import com.example.feedarticlescompose.ui.sharedComponents.ArticleItemExpandedContent
import com.example.feedarticlescompose.ui.sharedComponents.ArticleItemWhenAuthorContent
import com.example.feedarticlescompose.ui.sharedComponents.CategoryContent
import com.example.feedarticlescompose.ui.sharedComponents.DeleteDismissBoxComponent
import com.example.feedarticlescompose.ui.theme.BluePrimary
import com.example.feedarticlescompose.utils.CategoryUtils
import com.example.feedarticlescompose.utils.parsedDate

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    HomeContent(
        listOfArticles = listOf(
            ArticleDto(0,
                "3(Yffw{gmveK}MD1%cb{zg]pn(1=p&Yu:dha8LWu168L_+44{*JTi-:,b[Yrj26@EtM6UDDnR5qn]4qG",
                "xx",
                "https://cdn.countryflags.com/thumbs/india/flag-800.png",
                1,
                "x",
                1)
        ),
        goToAddArticleNav = {},
        logoutNav = {},
        1,
        getIdArticleOnItemClicked = {_, ->},
        onDelete = {_->},
        refreshList = {},
        getCategoryClicked = {_->}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    listOfArticles: List<ArticleDto> = emptyList(),
    goToAddArticleNav: () -> Unit,
    logoutNav: () -> Unit,
    userId: Long,
    getCategoryClicked : (Int) -> Unit,
    refreshList: () -> Unit,
    onDelete: (Long) -> Unit,
    getIdArticleOnItemClicked: (Long) -> Unit
){
    val context = LocalContext.current
    val isRefreshing by remember { mutableStateOf(false) }
    var categorySelected by rememberSaveable { mutableIntStateOf(R.string.all) }
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .systemBarsPadding()
    ){
       Column {
           Row(
               horizontalArrangement = Arrangement.SpaceBetween,
               modifier = Modifier.fillMaxWidth()
           ) {
               Icon(
                   imageVector = Icons.Rounded.Add,
                   contentDescription = context.getString(R.string.add),
                   modifier = Modifier
                       .size(35.dp)
                       .clickable {
                           goToAddArticleNav()
                       }
               )
               Icon(
                   imageVector = Icons.Rounded.PowerSettingsNew,
                   contentDescription = context.getString(R.string.add),
                   modifier = Modifier
                       .size(35.dp)
                       .clickable {
                           logoutNav()
                       }
               )
           }
           Spacer(modifier = Modifier.size(10.dp))
           Column(
               horizontalAlignment = Alignment.CenterHorizontally,
           ) {
               PullToRefreshBox(
                   state = pullToRefreshState,
                   isRefreshing = isRefreshing,
                   onRefresh = {
                       refreshList()
                   },
                  indicator = {
                      Indicator(
                          modifier = Modifier.align(Alignment.TopCenter),
                          isRefreshing = isRefreshing,
                          containerColor = Color.White,
                          color = BluePrimary,
                          state = pullToRefreshState
                      )
                  },
                   modifier = Modifier.weight(1f)
               ) {
                   LazyColumn(
                       modifier = Modifier
                   ) {
                       items(items = listOfArticles){
                           if (it.idU == userId)
                               ArticleItemWhenAuthorContent(
                                   it,
                                   onDelete,
                                   getIdArticleOnItemClicked
                               )
                           else
                               ArticleItemExpandedContent(it)

                       }
                   }
               }
               CategoryContent(
                   CategoryUtils.listOfCategoriesAndAll,
                   defaultValue = categorySelected
               ){
                   categorySelected = it
                   getCategoryClicked(it)
               }
           }
       }
    }
}

@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel){
    val context = LocalContext.current
    val listOfArticles by vm.articlesListStateFlow.collectAsState()
    var idArticle by remember {
        mutableLongStateOf(0L)
    }

    LaunchedEffect(key1 = true) {
        vm.getAllArticles()
    }

    LaunchedEffect(key1 = true) {
        vm.triggerNavigationToRegisterSharedFlow.collect{
            if(it)
                navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Home.route){
                        inclusive = true
                    }
                }
        }
    }

    LaunchedEffect(key1 = true) {
        vm.triggerNavigationToEditSharedFlow.collect{
            if(it)
                navController.navigate(Screen.Edit.route+"/$idArticle")
        }
    }

    LaunchedEffect(key1 = true) {
        vm.userMessageSharedFlow.collect{
            Toast.makeText(context, context.getString(it), Toast.LENGTH_SHORT).show()
        }
    }


    HomeContent(
        listOfArticles = listOfArticles,
        goToAddArticleNav = {
            navController.navigate(Screen.Create.route)
        },
        logoutNav = {
            vm.disconnectUser()
        },
        getCategoryClicked = {
            vm.filteredList(it)
        },
        userId = vm.getUserId(),
        getIdArticleOnItemClicked = {
            idArticle = it
            vm.setNavigationToEdit()
        },
        refreshList = {
            vm.getAllArticles()
        },
        onDelete = {vm.deleteArticle(it)}
    )

}