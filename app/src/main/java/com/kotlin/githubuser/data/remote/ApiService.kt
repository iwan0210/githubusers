package com.kotlin.githubuser.data.remote

import com.kotlin.githubuser.data.model.Github
import com.kotlin.githubuser.data.model.GithubUser
import com.kotlin.githubuser.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getListUsers(
        @Query("per_page") per_page: String
    ): List<Github>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): GithubUser

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") q: String,
        @Query("per_page") per_page: String
    ): SearchResponse

    @GET("users/{username}/followers")
    suspend fun getListFollowers(
        @Path("username") username: String,
    ): List<Github>

    @GET("users/{username}/following")
    suspend fun getListFollowing(
        @Path("username") username: String,
    ): List<Github>
}