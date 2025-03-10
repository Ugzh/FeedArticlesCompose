package com.example.feedarticlescompose.ui.screen.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedarticlescompose.network.MyPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(myPrefs: MyPrefs): ViewModel(){

    private var _userAlreadyConnectedSharedFlow =  MutableSharedFlow<Boolean>()

    val userAlreadyConnectedSharedFlow = _userAlreadyConnectedSharedFlow.asSharedFlow()
    init {
        viewModelScope.launch {
            delay(1500)
            _userAlreadyConnectedSharedFlow.emit(myPrefs.token != null)
        }
    }
}