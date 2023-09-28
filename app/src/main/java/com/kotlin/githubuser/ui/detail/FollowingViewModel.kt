package com.kotlin.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.githubuser.data.GithubRepository
import com.kotlin.githubuser.data.Result
import com.kotlin.githubuser.data.model.Github

class FollowingViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    private val _result = MutableLiveData<Result<List<Github>>>()
    val result: LiveData<Result<List<Github>>> = _result

    fun getFollowing(username: String) {
        githubRepository.getFollowing(username).observeForever {
            _result.value = it
        }
    }
}