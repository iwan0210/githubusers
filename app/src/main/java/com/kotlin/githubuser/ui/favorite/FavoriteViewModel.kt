package com.kotlin.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.githubuser.data.GithubRepository
import com.kotlin.githubuser.data.model.Github

class FavoriteViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    private val _result = MutableLiveData<List<Github>>()
    val result: LiveData<List<Github>> = _result

    fun getFavorites() {
        githubRepository.getFavorite().observeForever {
            _result.value = it
        }
    }

    fun searchFavorite(search: String) {
        githubRepository.searchFavorite(search).observeForever {
            _result.value = it
        }
    }
}