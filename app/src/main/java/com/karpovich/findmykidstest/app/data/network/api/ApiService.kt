package com.karpovich.findmykidstest.app.data.network.api

import com.karpovich.findmykidstest.app.data.network.entities.GitHubItemEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getGitHubUsers(@Query("per_page")perPage:Int): Response<List<GitHubItemEntity>>

    @GET("users/{username}")
    suspend fun getGitHubUser(@Path("username") username: String): Response<GitHubItemEntity>
}