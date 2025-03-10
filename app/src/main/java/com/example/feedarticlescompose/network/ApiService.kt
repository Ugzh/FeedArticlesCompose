package com.example.feedarticlescompose.network

import com.example.feedarticlescompose.network.dtos.article.ArticleDto
import com.example.feedarticlescompose.network.dtos.article.NewArticleDto
import com.example.feedarticlescompose.network.dtos.article.UpdateArticleDto
import com.example.feedarticlescompose.network.dtos.user.RegisterAndLoginResponseDto
import com.example.feedarticlescompose.network.dtos.user.RegisterDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @PUT(ApiRoutes.USER)
    suspend fun register(
        @Body registerDto: RegisterDto
    ): Response<RegisterAndLoginResponseDto>?

    @FormUrlEncoded
    @POST(ApiRoutes.USER)
    suspend fun login(
        @Field("login") login: String,
        @Field("mdp") password: String,
    ): Response<RegisterAndLoginResponseDto>?

    @GET(ApiRoutes.ARTICLES)
    suspend fun getAllArticles(
        @Header("token") token: String
    ) : Response<List<ArticleDto>>?

    @GET(ApiRoutes.ARTICLES+"{id}")
    suspend fun getArticle(
        @Path("id") idArticle: Long,
        @Header("token") token: String
    ): Response<ArticleDto>?

    @POST(ApiRoutes.ARTICLES+"/{id}")
    suspend fun updateArticle(
        @Path("id") idArticle: Long,
        @Header("token") token: String,
        @Body updateArticleDto: UpdateArticleDto
    ): Response<Unit>?

    @DELETE(ApiRoutes.ARTICLES+"/id")
    suspend fun deleteArticle(
        @Path("id") idArticle: Long,
        @Header("token") token: String,
    ): Response<Unit>?

    @PUT(ApiRoutes.ARTICLES)
    suspend fun createArticle(
        @Header("token") token: String,
        @Body newArticleDto: NewArticleDto
    ): Response<Unit>?
}