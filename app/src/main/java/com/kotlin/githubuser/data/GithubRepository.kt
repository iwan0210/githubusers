package com.kotlin.githubuser.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kotlin.githubuser.data.datastore.SettingPreferences
import com.kotlin.githubuser.data.local.GithubDatabase
import com.kotlin.githubuser.data.model.Github
import com.kotlin.githubuser.data.model.GithubUser
import com.kotlin.githubuser.data.remote.ApiService
import kotlinx.coroutines.flow.Flow

class GithubRepository(
    private val settingPreferences: SettingPreferences,
    private val githubDatabase: GithubDatabase,
    private val apiService: ApiService
) {
    fun getListUsers(): LiveData<Result<List<Github>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getListUsers(PER_PAGE)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun searchUser(q: String): LiveData<Result<List<Github>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchUsers(q, PER_PAGE)
            emit(Result.Success(response.items))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowers(username: String): LiveData<Result<List<Github>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getListFollowers(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFollowing(username: String): LiveData<Result<List<Github>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getListFollowing(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserByUsername(username: String): LiveData<Result<GithubUser>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavorite(): LiveData<List<Github>> = githubDatabase.githubDao().getAllFavorite()

    fun searchFavorite(search: String): LiveData<List<Github>> = githubDatabase.githubDao().searchFavorite(search)

    suspend fun insertFavorite(github: Github) {
        githubDatabase.githubDao().insert(github)
    }

    suspend fun deleteFavorite(github: Github) {
        githubDatabase.githubDao().delete(github)
    }

    fun isExist(username: String): Flow<Boolean> = githubDatabase.githubDao().checkFavorite(username)

    fun getThemeSetting(): Flow<Boolean> = settingPreferences.getThemeSetting()

    suspend fun setThemeSetting(isDark: Boolean) = settingPreferences.saveThemeSetting(isDark)

    companion object {
        private const val PER_PAGE = "15"
    }
}