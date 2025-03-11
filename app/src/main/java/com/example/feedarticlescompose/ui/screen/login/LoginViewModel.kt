package com.example.feedarticlescompose.ui.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.ApiService
import com.example.feedarticlescompose.network.MyPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: MyPrefs
): ViewModel() {

    private var _userMessageSharedFlow = MutableSharedFlow<Int>()
    var userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    private var _triggerNavigationToHome = MutableSharedFlow<Boolean>()
    var triggerNavigationToHome = _triggerNavigationToHome.asSharedFlow()

    fun logUser(login: String, password: String) {
        val trimLogin = login.trim()
        val trimPassword = password.trim()
        if (trimLogin.isNotEmpty() && trimPassword.isNotEmpty()){
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO){
                        db.login(trimLogin, trimPassword)
                    }

                    val body = response?.body()

                    when{
                        response == null ->
                            _userMessageSharedFlow.emit(R.string.no_response_database)

                        response.code() != 0 ->
                            when(response.code()){
                                200 -> {
                                    myPrefs.token = body!!.token
                                    myPrefs.userId = body.id
                                    _triggerNavigationToHome.emit(true)
                                    R.string.login
                                }
                                304 -> R.string.internal_problem
                                400 -> R.string.check_all_fields
                                401 -> R.string.wrong_informations
                                503 -> R.string.error_from_database
                                else -> return@launch
                            }.let {
                                _userMessageSharedFlow.emit(it)
                            }
                    }
                } catch (ce: ConnectException){
                    viewModelScope.launch {
                        _userMessageSharedFlow.emit(R.string.password_not_match)
                    }
                }
            }
        } else
            viewModelScope.launch {
                _userMessageSharedFlow.emit(R.string.fill_fields)
            }
    }
}