package com.example.feedarticlescompose.ui.sharedComponents

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.dtos.article.ArticleDto


@Preview(showBackground = true)
@Composable
fun SwipeToDismissComponentPreview(){
    /*SwipeToDismissComponent("salut", {s ->  }, {
        Text(text = "test", Modifier.fillMaxWidth().size(45.dp))
    })*/
}

@Composable
fun SwipeToDismissComponent(
    item: String ,
    onDelete: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            if(newValue == SwipeToDismissBoxValue.EndToStart){
                onDelete(item)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier,
        backgroundContent = {
            if (dismissState.dismissDirection.name == SwipeToDismissBoxValue.EndToStart.name) {
                Row(modifier = Modifier
                    //.fillMaxSize()
                    .background(Color.Red),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "delete")
                }
            }
        },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        content = {content()}
    )
}

@Composable
fun DeleteDissmissBoxComponent(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it){
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true
                }
                else -> return@rememberSwipeToDismissBoxState false
            }
        },
        positionalThreshold = { it * 0.25f}
    )
    val color = when(swipeState.dismissDirection){
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF0000)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
        else -> return
    }


    SwipeToDismissBox(
        state = swipeState,
        backgroundContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxSize()
                    .background(color)

            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.White,
                    contentDescription = context.getString(R.string.delete),
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }
    ) {
        content()
    }
}
