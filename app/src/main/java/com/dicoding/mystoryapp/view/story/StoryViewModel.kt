package com.dicoding.mystoryapp.view.story

import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.repository.StoryRepository
import java.io.File

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun uploadImage(getFile: File?, description: String, lat: Float? = null, lon: Float? = null) =
        storyRepository.uploadImage(getFile, description, lat, lon)
}