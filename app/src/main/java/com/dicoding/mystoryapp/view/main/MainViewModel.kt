package com.dicoding.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dicoding.mystoryapp.Result
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.remote.StoryListItem
import com.dicoding.mystoryapp.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(storyRepository: StoryRepository, private val preferences: TokenPreferences) :
    ViewModel() {

    val getStories: LiveData<Result<PagingData<StoryListItem>>> by lazy {
        storyRepository.getStoriesPaging(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            preferences.logout()
        }
    }

}