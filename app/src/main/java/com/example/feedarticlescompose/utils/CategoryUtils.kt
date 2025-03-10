package com.example.feedarticlescompose.utils

import androidx.compose.ui.graphics.Color
import com.example.feedarticlescompose.R

object CategoryUtils{
    val listOfCategoriesAndAll =  listOf(R.string.all, R.string.sport, R.string.manga, R.string.various)
    val listOfCategories =  listOf(R.string.sport, R.string.manga, R.string.various)
    fun getCategoryIdString(catIdButton : Int): Int{
        return when (catIdButton) {
            R.string.sport -> 1
            R.string.manga -> 2
            R.string.various -> 3
            else -> 0
        }
    }

    fun getColor(cat: Int): Color {
        return when(cat){
            1 -> Color.Yellow
            2 -> Color.Cyan
            3 -> Color.Green
            else -> Color.Transparent
        }
    }

    fun getCategoryByNumber(cat: Int): Int {
        return when(cat){
            1 -> R.string.sport
            2 -> R.string.manga
            3 -> R.string.various
            else -> 0
        }
    }
}