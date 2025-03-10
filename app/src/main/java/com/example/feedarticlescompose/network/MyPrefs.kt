package com.example.feedarticlescompose.network

import android.content.SharedPreferences
import com.example.feedarticlescompose.utils.TOKEN
import com.example.feedarticlescompose.utils.USER_ID
import javax.inject.Inject

class MyPrefs @Inject constructor(private val sharedPreferences: SharedPreferences){
    var userId : Long
        get() = sharedPreferences.getLong(USER_ID, 0L)
        set(value) = sharedPreferences.edit().putLong(USER_ID,value).apply()


    var token: String?
        get() = sharedPreferences.getString(TOKEN,null)
        set(value) = sharedPreferences.edit().putString(TOKEN, value).apply()

}