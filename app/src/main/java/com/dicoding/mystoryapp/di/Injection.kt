package com.dicoding.mystoryapp.di

import android.content.Context
import com.dicoding.mystoryapp.data.local.StoryDatabase
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.preference.dataStore
import com.dicoding.mystoryapp.data.remote.ApiConfig
import com.dicoding.mystoryapp.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = TokenPreferences.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getToken().first()
        }
        val apiService = ApiConfig.getApiConfig(token.toString())
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, pref, storyDatabase)
    }
}