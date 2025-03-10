package com.example.feedarticlescompose.network.dtos.user


import com.squareup.moshi.Json

data class RegisterAndLoginResponseDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "token")
    val token: String
)