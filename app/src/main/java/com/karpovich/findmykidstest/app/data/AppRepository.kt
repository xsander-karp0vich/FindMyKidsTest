package com.karpovich.findmykidstest.app.data

import com.karpovich.findmykidstest.app.data.network.api.ApiFactory
import com.karpovich.findmykidstest.app.data.network.api.ApiService
import com.karpovich.findmykidstest.app.data.network.entities.GitHubItemEntity
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import retrofit2.Response

object AppRepository : ApiService {
    private val api = ApiFactory().apiService
    override suspend fun getGitHubUsers(perPage:Int): Response<List<GitHubItemEntity>> {
        return api.getGitHubUsers(perPage)
    }

    override suspend fun getGitHubUser(username: String): Response<GitHubUserEntity> {
        return api.getGitHubUser(username)
    }

    override suspend fun getGitHubFollowers(username: String): Response<List<GitHubItemEntity>> {
        return api.getGitHubFollowers(username)
    }
}