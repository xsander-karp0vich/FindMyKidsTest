package com.karpovich.findmykidstest.app.data

import com.karpovich.findmykidstest.app.data.network.api.ApiFactory
import com.karpovich.findmykidstest.app.data.network.api.ApiService
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

object AppRepository : ApiService {
    private val api = ApiFactory().apiService
    override suspend fun getGitHubUsers(
        @Query(value = "per_page") perPage: Int,
        @Query(value = "since") since: Int
    ): Response<List<GitHubUserEntity>> {
        return api.getGitHubUsers(perPage,since)
    }

    override suspend fun getGitHubUser(username: String): Response<GitHubUserEntity> {
        return api.getGitHubUser(username)
    }

    override suspend fun getGitHubFollowers(
        @Path(value = "username") username: String,
        @Query(value = "per_page") perPage: Int,
    ): Response<List<GitHubUserEntity>> {
        return api.getGitHubFollowers(username,perPage)
    }
}