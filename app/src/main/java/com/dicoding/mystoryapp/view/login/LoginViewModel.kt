package com.dicoding.mystoryapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.Result
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.remote.LoginResult
import com.dicoding.mystoryapp.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val storyRepository: StoryRepository,
    private val preferences: TokenPreferences
) : ViewModel() {
    fun login(emailInput: String, passwordInput: String) : LiveData<Result<LoginResult>> {
        return storyRepository.login(emailInput, passwordInput)
    }

    fun saveState(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
            preferences.login()
        }
    }
}