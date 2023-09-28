package com.kotlin.githubuser.ui.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kotlin.githubuser.data.GithubRepository
import com.kotlin.githubuser.utils.MainDispatcherRule
import com.kotlin.githubuser.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var githubRepository: GithubRepository
    private lateinit var settingViewModel: SettingViewModel

    @Before
    fun setUp() {
        settingViewModel = SettingViewModel(githubRepository)
    }

    @Test
    fun getTheme() {
        val expected: Flow<Boolean> = flow {
            emit(false)
        }
        `when`(githubRepository.getThemeSetting()).thenReturn(expected)
        val actual = settingViewModel.getTheme().getOrAwaitValue()
        Mockito.verify(githubRepository).getThemeSetting()
        assertNotNull(actual)
        assertEquals(false, actual)
    }

    @Test
    fun setTheme() = runTest {
        settingViewModel.setTheme(true)
        Mockito.verify(githubRepository).setThemeSetting(true)
    }
}