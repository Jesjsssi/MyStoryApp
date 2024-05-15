package com.dicoding.mystoryapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.di.Injection
import com.dicoding.mystoryapp.repository.StoryRepository
import com.dicoding.mystoryapp.view.login.LoginViewModel
import com.dicoding.mystoryapp.view.main.MainViewModel
import com.dicoding.mystoryapp.view.maps.MapViewModel
import com.dicoding.mystoryapp.view.signup.SignupViewModel
import com.dicoding.mystoryapp.view.story.StoryViewModel

class ViewModelFactory(
    private val repository: StoryRepository,
    private val preferences: TokenPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                return SignupViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(repository, preferences) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(repository, preferences) as T
            }

            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                return StoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                return MapViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context, preferences: TokenPreferences): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    preferences
                ).also { INSTANCE = it }
            }
        }
    }
}