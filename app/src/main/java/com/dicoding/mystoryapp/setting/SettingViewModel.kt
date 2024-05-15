package com.dicoding.mystoryapp.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.data.preference.LanguagePreferences
import kotlinx.coroutines.launch
import java.util.Locale

class SettingViewModel(private val languagePreferences: LanguagePreferences) : ViewModel() {
    fun getLanguagePreference(): LiveData<Locale> {
        return languagePreferences.getLanguagePreference().asLiveData()
    }

    fun setLanguagePreference(language: String) {
        viewModelScope.launch {
            languagePreferences.setLanguage(language)
        }

    }

}