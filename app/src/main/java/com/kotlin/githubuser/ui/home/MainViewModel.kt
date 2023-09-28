package com.kotlin.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kotlin.githubuser.data.GithubRepository
import com.kotlin.githubuser.data.Result
import com.kotlin.githubuser.data.model.Github

class MainViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    private val _result = MutableLiveData<Result<List<Github>>>()
    val result: LiveData<Result<List<Github>>> = _result

    init {
        getUsers()
    }

    fun getUsers() {
        githubRepository.getListUsers().observeForever {
            _result.value = it
        }
    }

    fun searchUser(keyword: String) {
        githubRepository.searchUser(keyword).observeForever {
            _result.value = it
        }
    }

    fun getTheme(): LiveData<Boolean> = githubRepository.getThemeSetting().asLiveData()
}