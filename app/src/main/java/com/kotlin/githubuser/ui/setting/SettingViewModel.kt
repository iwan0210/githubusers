package com.kotlin.githubuser.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlin.githubuser.data.GithubRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    fun getTheme(): LiveData<Boolean> = githubRepository.getThemeSetting().asLiveData()

    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            githubRepository.setThemeSetting(isDark)
        }
    }
}