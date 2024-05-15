package com.dicoding.mystoryapp.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class LanguagePreferences(private val dataStore: DataStore<Preferences>) {
    fun getLanguagePreference(): Flow<Locale> {
        return dataStore.data.map { preferences ->
            val languageCode = preferences[PREF_LANGUAGE] ?: Locale.getDefault().language
            Locale(languageCode)
        }
    }

    suspend fun setLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[PREF_LANGUAGE] = languageCode
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LanguagePreferences? = null
        private val PREF_LANGUAGE = stringPreferencesKey("locale")
        fun getInstance(dataStore: DataStore<Preferences>): LanguagePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LanguagePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}