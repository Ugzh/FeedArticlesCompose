package com.example.feedarticlescompose.ui.screen.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.ApiService
import com.example.feedarticlescompose.network.MyPrefs
import com.example.feedarticlescompose.network.dtos.article.ArticleDto
import com.example.feedarticlescompose.utils.CategoryUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val myPrefs: MyPrefs,
    private val db: ApiService
): ViewModel() {

    private var _articles = arrayListOf<ArticleDto>()

    private var _articlesListStateFlow = MutableStateFlow<List<ArticleDto>>(emptyList())
    val articlesListStateFlow = _articlesListStateFlow.asStateFlow()

    private var _userMessageSharedFlow = MutableSharedFlow<Int>()
    var userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    private var _triggerNavigationToRegisterSharedFlow = MutableSharedFlow<Boolean>()
    var triggerNavigationToRegisterSharedFlow =
        _triggerNavigationToRegisterSharedFlow.asSharedFlow()

    private var _triggerNavigationToEditSharedFlow = MutableSharedFlow<Boolean>()
    var triggerNavigationToEditSharedFlow =
        _triggerNavigationToEditSharedFlow.asSharedFlow()

    private var categorySelected = 0

    init {
        getAllArticles()
    }
    fun disconnectUser(){
        myPrefs.token = null
        myPrefs.userId = 0
        viewModelScope.launch {
            _triggerNavigationToRegisterSharedFlow.emit(true)
        }
    }
    fun getAllArticles(){
        myPrefs.token?.let {
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO){
                        db.getAllArticles(token = it)
                    }

                    val body = response?.body()

                    when{
                        response == null ->
                            _userMessageSharedFlow.emit(R.string.no_response_database)
                        response.code() != 0 ->
                            when(response.code()){
                                200 -> {
                                    if(categorySelected != 0){
                                        _articlesListStateFlow.value = body!!.filter {
                                          it.categorie == categorySelected
                                        }
                                    } else {
                                        _articlesListStateFlow.value = body!!
                                    }
                                    _articles.addAll(body)
                                }
                                400, 401 -> {
                                    disconnectUser()
                                    _userMessageSharedFlow
                                        .emit(R.string.error_from_database_redirection)
                                }
                                503 -> _userMessageSharedFlow.emit(R.string.no_response_database)
                                else -> return@launch
                            }
                    }
                } catch (ce: ConnectException){
                    viewModelScope.launch {
                        _userMessageSharedFlow.emit(R.string.error_from_database)
                    }
                }
            }
        }
    }

    fun filteredList(idCategory: Int){
        val cat = CategoryUtils.getCategoryIdString(idCategory)
        categorySelected = cat
        _articlesListStateFlow.value =
            if(cat != 0)
                _articles.filter {
                    it.categorie == cat
                }
            else _articles
    }
    fun getUserId(): Long{
        return myPrefs.userId
    }

    fun setNavigationToEdit(){
        viewModelScope.launch {
            _triggerNavigationToEditSharedFlow.emit(true)
        }
    }
    fun deleteArticle(idArticle: Long){
        try {
            viewModelScope.launch{
                val response = withContext(Dispatchers.IO){
                    db.deleteArticle(idArticle, myPrefs.token!!)
                }

                when{
                    response == null -> _userMessageSharedFlow.emit(R.string.no_response_database)
                    response.code() != 0 ->
                        when(response.code()){
                            201 -> {
                                _userMessageSharedFlow.emit(R.string.article_deleted)
                                getAllArticles()
                            }
                            304 -> _userMessageSharedFlow.emit(R.string.article_not_deleted)
                            400 ,401 -> {
                                disconnectUser()
                                _userMessageSharedFlow
                                    .emit(R.string.error_from_database_redirection)
                            }
                            503 -> _userMessageSharedFlow.emit(R.string.no_response_database)
                            else -> return@launch
                        }
                }
            }
        } catch (ce: ConnectException){
            viewModelScope.launch {
                _userMessageSharedFlow.emit(R.string.error_from_database)
            }
        }
    }

}