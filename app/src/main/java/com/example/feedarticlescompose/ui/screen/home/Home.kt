package com.example.feedarticlescompose.ui.screen.home

import android.content.ClipData.Item
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.ImagePainter
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.dtos.article.ArticleDto
import com.example.feedarticlescompose.ui.sharedComponents.CategoryContent

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
        )
    )
}

@Composable
fun HomeContent(listOfArticles: List<ArticleDto> = emptyList()){
    val context = LocalContext.current
    val radioListOptions = listOf(R.string.all, R.string.sport, R.string.manga, R.string.various)
    var idSelect = 0
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ){
       Column {
           Row(
               horizontalArrangement = Arrangement.SpaceBetween,
               modifier = Modifier.fillMaxWidth()
           ) {
               Icon(
                   imageVector = Icons.Rounded.Add,
                   contentDescription = context.getString(R.string.add)
               )
               Icon(
                   imageVector = Icons.Rounded.PowerSettingsNew,
                   tint = Color.Gray,
                   contentDescription = context.getString(R.string.add)
               )
           }
           Spacer(modifier = Modifier.size(10.dp))
           Column(
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               LazyColumn(
                   modifier = Modifier
                       .weight(1f)
               ) {
                   items(items = listOfArticles){
                       Row(
                           verticalAlignment = Alignment.CenterVertically,
                           modifier = Modifier
                               .padding(vertical = 5.dp)
                               .border(
                                   BorderStroke(1.dp, Color.Black),
                                   shape = RoundedCornerShape(10.dp)
                               )
                               .background(
                                   getColor(it.categorie),
                                   shape = RoundedCornerShape(10.dp)
                               )
                               .fillMaxWidth()
                               .padding(10.dp)
                       ) {
                           AsyncImage(
                               model = it.urlImage,
                               contentDescription = it.titre,
                               contentScale = ContentScale.FillHeight,
                               error = painterResource(id = R.drawable.feedarticles_logo),
                               modifier = Modifier
                                   .size(50.dp)
                                   .clip(CircleShape)
                                   .border(
                                       BorderStroke(1.dp, Color.Gray),
                                       CircleShape
                                   )
                           )
                           Text(
                               text = it.titre,
                               maxLines = 2,
                               overflow = TextOverflow.Ellipsis,
                               modifier = Modifier.padding(10.dp)
                           )
                       }
                   }
               }
               CategoryContent(radioListOptions){
                    idSelect = it
               }
           }
       }
    }
}

@Composable
fun HomeScreen(navController: NavController, vm: HomeViewModel){
    val context = LocalContext.current
    val listOfArticles by vm.articlesListStateFlow.collectAsState()
    HomeContent(listOfArticles = listOfArticles)
}

private fun getColor(cat: Int): Color{
    return when(cat){
        1 -> Color.Yellow
        2 -> Color.Cyan
        3 -> Color.Green
        else -> Color.Transparent
    }
}