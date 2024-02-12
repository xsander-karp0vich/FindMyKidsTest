package com.karpovich.findmykidstest.app.data.network.entities

import com.google.gson.annotations.SerializedName

data class GitHubUserEntity(
    val login: String?,
    val id: Int?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    val url: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("company") val company: String?,
    @SerializedName("blog") val blog: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("twitter_username") val twitterUsername: String?,
    @SerializedName("public_repos") var publicRepos: Int?,
    @SerializedName("followers") var followers: Int?,
    @SerializedName("following") val following: Int?,
)
