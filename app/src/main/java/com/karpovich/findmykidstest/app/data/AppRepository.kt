package com.karpovich.findmykidstest.app.data

import com.karpovich.findmykidstest.app.data.network.api.ApiFactory
import com.karpovich.findmykidstest.app.data.network.api.ApiService
import com.karpovich.findmykidstest.app.data.network.entities.GitHubItemEntity
import retrofit2.Response

object AppRepository : ApiService {
    private val api = ApiFactory().apiService
    override suspend fun getGitHubUsers(perPage:Int): Response<List<GitHubItemEntity>> {
        return api.getGitHubUsers(perPage)
    }

    override suspend fun getGitHubUser(username: String): Response<GitHubItemEntity> {
        return api.getGitHubUser(username)
    }
}