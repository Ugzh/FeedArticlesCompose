package com.example.feedarticlescompose.ui.screen.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlescompose.R
import com.example.feedarticlescompose.network.ApiService
import com.example.feedarticlescompose.network.MyPrefs
import com.example.feedarticlescompose.network.dtos.user.RegisterDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val db: ApiService,
    private val myPrefs: MyPrefs
) : ViewModel() {

    private var _userMessageSharedFlow = MutableSharedFlow<Int>()
    var userMessageSharedFlow = _userMessageSharedFlow.asSharedFlow()


    private var _triggerNavigationToLogin = MutableSharedFlow<Boolean>()
    var triggerNavigationToLogin = _triggerNavigationToLogin.asSharedFlow()

    fun registerUser(
        login: String,
        password: String,
        confirmPassword: String
    ){
        val trimLogin = login.trim()
        val trimPassword = password.trim()
        val trimConfirmPassword = confirmPassword.trim()
        if(
            trimLogin.isNotEmpty()
            && trimPassword.isNotEmpty()
            && trimConfirmPassword.isNotEmpty()
        ){
            if(trimPassword == trimConfirmPassword){
                viewModelScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO){
                            db.register(RegisterDto(login,password))
                        }
                        val body = response?.body()

                        when{
                            response == null -> R.string.no_response_database
                            response.code() != 0 ->
                                when(response.code()){
                                    200 -> {
                                        myPrefs.token = body!!.token
                                        myPrefs.userId = body.id
                                        _triggerNavigationToLogin.emit(true)
                                        R.string.account_created
                                    }
                                    303 -> R.string.login_already_used
                                    304 -> R.string.account_not_created
                                    400 -> R.string.check_all_fields
                                    503 -> R.string.error_from_database
                                    else -> return@launch
                                }
                            else -> return@launch
                        }.let {
                            _userMessageSharedFlow.emit(it)
                        }
                    } catch (ce: ConnectException){
                        viewModelScope.launch {
                            _userMessageSharedFlow.emit(R.string.no_response_database)
                        }
                    }
                }
            } else
               viewModelScope.launch {
                   _userMessageSharedFlow.emit(R.string.password_not_match)
               }

        } else
            viewModelScope.launch {
                _userMessageSharedFlow.emit(R.string.fill_fields)
            }
    }
}