package com.kotlin.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.githubuser.data.GithubRepository
import com.kotlin.githubuser.data.model.Github
import kotlinx.coroutines.launch

class DetailViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    fun getUserByUsername(username: String) = githubRepository.getUserByUsername(username)

    fun isFavorite(username: String): LiveData<Boolean> = githubRepository.isExist(username).asLiveData()

    fun deleteFavorite(github: Github) {
        viewModelScope.launch {
            githubRepository.deleteFavorite(github)
        }
    }

    fun insertFavorite(github: Github) {
        viewModelScope.launch {
            githubRepository.insertFavorite(github)
        }
    }
}