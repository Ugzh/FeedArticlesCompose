package com.example.feedarticlescompose.ui.sharedComponents

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.dtos.article.ArticleDto
import com.example.feedarticlescompose.utils.CategoryUtils
import com.example.feedarticlescompose.utils.parsedDate
import kotlinx.coroutines.delay

@Composable
fun ArticleItemExpandedContent(article: ArticleDto){
    val context = LocalContext.current
    var isExpended by remember(article.id) {
        mutableStateOf(false)
    }
    val sizeImage = if(isExpended) 70.dp else 50.dp
    var articleChanged by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(article.id) {
        articleChanged = true
        delay(100)
        articleChanged = false
    }

    Box(Modifier.clickable { isExpended = !isExpended }){
        Column(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .border(
                    BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    CategoryUtils.getColor(article.categorie),
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .padding(10.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ) {
                AsyncImage(
                    model = article.urlImage,
                    contentDescription = article.titre,
                    contentScale = ContentScale.FillHeight,
                    error = painterResource(id = R.drawable.feedarticles_logo),
                    modifier = Modifier
                        .size(sizeImage)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(1.dp, Color.Gray),
                            CircleShape
                        )
                )
                Text(
                    text = article.titre,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                )
                if(isExpended)
                    Icon(
                        imageVector =  Icons.Outlined.ArrowDropUp ,
                        contentDescription = "arrow",
                    )
            }
            if(!articleChanged)
                AnimatedVisibility(
                    visible = isExpended,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300))
                            + expandVertically(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 150))
                            + slideOutVertically(animationSpec = tween(durationMillis = 200))
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = context
                                    .getString(
                                        R.string.created_at,
                                        parsedDate(article.createdAt)
                                    ),
                                fontSize = 12.sp
                            )
                            Text(
                                text = context
                                    .getString(
                                        R.string.category_type,
                                        context.getString(
                                            CategoryUtils
                                                .getCategoryByNumber(article.categorie)
                                        )
                                    ),
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = article.descriptif,
                            modifier = Modifier.padding(top = 10.dp),
                            fontSize = 12.sp
                        )
                    }
            }

        }
    }
}

@Composable
fun ArticleItemWhenAuthorContent(
    article: ArticleDto,
    onDelete: (Long) -> Unit,
    getIdArticleOnItemClicked: (Long) -> Unit
){
    DeleteDismissBoxComponent({ onDelete(article.id)}) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .clickable {
                    getIdArticleOnItemClicked(article.id)
                }
                .border(
                    BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    CategoryUtils.getColor(article.categorie),
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .padding(10.dp)

        ) {
            AsyncImage(
                model = article.urlImage,
                contentDescription = article.titre,
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
                text = article.titre,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}
