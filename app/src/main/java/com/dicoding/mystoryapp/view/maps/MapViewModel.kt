package com.dicoding.mystoryapp.view.maps

import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.repository.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStoriesWithLocation() = storyRepository.getStoriesWithLocation()
}