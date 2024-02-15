package com.karpovich.findmykidstest.app.data.network.api

import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getGitHubUsers(
        @Query("per_page")perPage:Int,
        @Query("since") since: Int
    ): Response<List<GitHubUserEntity>>

    @GET("users/{username}")
    suspend fun getGitHubUser(@Path("username") username: String): Response<GitHubUserEntity>
    @GET("users/{username}/followers")
    suspend fun getGitHubFollowers(
        @Path("username") username: String,
        @Query("per_page")perPage: Int
    ): Response<List<GitHubUserEntity>>
}