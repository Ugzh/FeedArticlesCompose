package com.example.feedarticlescompose.ui.sharedComponents

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedarticlescompose.R

@Preview(showBackground = true)
@Composable
fun CategoryPreview(){
    val radioListOptions = listOf(R.string.all, R.string.sport, R.string.manga, R.string.various)
    CategoryContent(radioOptions = radioListOptions, defaultValue = R.string.all){_ ->
    }
}

@Composable
fun CategoryContent(
    radioOptions: List<Int> = emptyList(),
    defaultValue: Int = 0,
    getIdSelected: (Int) ->Unit
){
    val context = LocalContext.current

    var selectedOption by remember{
        mutableIntStateOf(defaultValue)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        radioOptions.forEach{ idString ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(
                        selected = idString == selectedOption,
                        onClick = {
                            selectedOption = idString
                            getIdSelected(idString)
                                  },
                        role = Role.RadioButton
                    ),
            ) {
                RadioButton(
                    selected = idString == selectedOption,
                    onClick = null
                )
                Text(text = context.getString(idString))
            }
        }
    }
}
