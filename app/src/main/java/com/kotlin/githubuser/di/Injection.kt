package com.kotlin.githubuser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kotlin.githubuser.data.GithubRepository
import com.kotlin.githubuser.data.datastore.SettingPreferences
import com.kotlin.githubuser.data.local.GithubDatabase
import com.kotlin.githubuser.data.remote.ApiConfig

val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings")

object Injection {
    fun provideRepository(context: Context): GithubRepository {
        val datastore = context.datastore
        val settingPreferences = SettingPreferences.getInstance(datastore)
        val database = GithubDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return GithubRepository(settingPreferences, database, apiService)
    }
}