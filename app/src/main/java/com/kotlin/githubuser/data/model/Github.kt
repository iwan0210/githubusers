package com.kotlin.githubuser.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Github(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "login")
    @field:SerializedName("login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    @field:SerializedName("avatar_url")
    val avatar_url: String
)

@Entity
data class GithubUser(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "login")
    @field:SerializedName("login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    @field:SerializedName("avatar_url")
    val avatar_url: String,

    @ColumnInfo(name = "name")
    @field:SerializedName("name")
    val name: String,

    @ColumnInfo(name = "public_repos")
    @field:SerializedName("public_repos")
    val public_repos: Int,

    @ColumnInfo(name = "followers")
    @field:SerializedName("followers")
    val followers: Int,

    @ColumnInfo(name = "following")
    @field:SerializedName("following")
    val following: Int
)

data class SearchResponse(
    @field:SerializedName("items")
    val items: List<Github>
)