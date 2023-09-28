package com.kotlin.githubuser.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlin.githubuser.data.GithubRepository
import com.kotlin.githubuser.di.Injection
import com.kotlin.githubuser.ui.detail.DetailViewModel
import com.kotlin.githubuser.ui.detail.FollowersViewModel
import com.kotlin.githubuser.ui.detail.FollowingViewModel
import com.kotlin.githubuser.ui.favorite.FavoriteViewModel
import com.kotlin.githubuser.ui.home.MainViewModel
import com.kotlin.githubuser.ui.setting.SettingViewModel

class ViewModelFactory private constructor(private val githubRepository: GithubRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(githubRepository) as T
        if (modelClass.isAssignableFrom(DetailViewModel::class.java))
            return DetailViewModel(githubRepository) as T
        if (modelClass.isAssignableFrom(FollowersViewModel::class.java))
            return FollowersViewModel(githubRepository) as T
        if (modelClass.isAssignableFrom(FollowingViewModel::class.java))
            return FollowingViewModel(githubRepository) as T
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java))
            return FavoriteViewModel(githubRepository) as T
        if (modelClass.isAssignableFrom(SettingViewModel::class.java))
            return SettingViewModel(githubRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}