package com.dicoding.mystoryapp.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.repository.StoryRepository

class SignupViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun register(nameInput: String, emailInput: String, passwordInput: String) =
        storyRepository.register(nameInput, emailInput, passwordInput)
}