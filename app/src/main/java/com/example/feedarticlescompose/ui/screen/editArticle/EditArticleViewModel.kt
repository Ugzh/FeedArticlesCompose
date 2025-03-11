package com.example.feedarticlescompose.ui.screen.editArticle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.ApiService
import com.example.feedarticlescompose.network.MyPrefs
import com.example.feedarticlescompose.network.dtos.article.ArticleDto
import com.example.feedarticlescompose.network.dtos.article.UpdateArticleDto
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
class EditArticleViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: MyPrefs
) : ViewModel() {

    private var _userMessageSharedFlow = MutableSharedFlow<Int>()
    var userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    private var _triggerNavigationToHomeSharedFlow =
        MutableSharedFlow<Boolean>()
    var triggerNavigationToHomeSharedFlow =
        _triggerNavigationToHomeSharedFlow.asSharedFlow()

    private var _articleStateFlow = MutableStateFlow<ArticleDto?>(null)
    val articleStateFlow = _articleStateFlow.asStateFlow()

    fun getArticle(idArticle: Long){
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    db.getArticle(idArticle, myPrefs.token!!)
                }
                val body = response?.body()

                when {
                    response == null -> R.string.no_response_database
                    response.code() != 0 -> {
                        when (response.code()) {
                            200 -> {
                                _articleStateFlow.value = body!!
                            }
                            303 -> {
                                _triggerNavigationToHomeSharedFlow.emit(true)
                                R.string.article_not_found
                            }
                            400 -> R.string.check_all_fields
                            401 -> {
                                _triggerNavigationToHomeSharedFlow.emit(true)
                                R.string.please_logout
                            }
                            503 -> R.string.error_from_database
                            else -> return@launch
                        }
                    }
                }
            } catch (ce: ConnectException) {
                viewModelScope.launch {
                    _userMessageSharedFlow.emit(R.string.error_from_database)
                }
            }
        }
    }

    fun updateArticle(title: String, desc: String, image: String, idButton: Int) {
        val trimTitle = title.trim()
        val trimDesc = desc.trim()
        val trimImage = image.trim()
        val cat = CategoryUtils.getCategoryIdString(idButton)

        if (
            trimTitle.isNotEmpty()
            && trimDesc.isNotEmpty()
            && trimImage.isNotEmpty()
            && cat != 0
        ) {
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        db.updateArticle(
                            _articleStateFlow.value!!.id, myPrefs.token!!,
                            UpdateArticleDto(
                                _articleStateFlow.value!!.id,
                                trimTitle,
                                trimDesc,
                                trimImage,
                                cat
                            )
                        )
                    }

                    when {
                        response == null -> R.string.no_response_database
                        response.code() != 0 -> {
                            when (response.code()) {
                                201 -> {
                                    _triggerNavigationToHomeSharedFlow.emit(true)
                                    R.string.article_updated
                                }
                                303 -> R.string.article_not_created
                                304 -> R.string.article_content_same
                                400 -> R.string.check_all_fields
                                401 -> R.string.please_logout
                                503 -> R.string.error_from_database
                                else -> return@launch
                            }
                        }
                        else -> return@launch
                    }.let {
                        _userMessageSharedFlow.emit(it)
                    }
                } catch (ce: ConnectException) {
                    viewModelScope.launch {
                        _userMessageSharedFlow.emit(R.string.error_from_database)
                    }
                }
            }
        } else
            viewModelScope.launch {
                _userMessageSharedFlow.emit(R.string.fill_fields)
            }
    }

}