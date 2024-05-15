package com.dicoding.mystoryapp.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.SettingViewModelFactory
import com.dicoding.mystoryapp.data.preference.LanguagePreferences
import com.dicoding.mystoryapp.databinding.ActivitySettingBinding
import com.dicoding.mystoryapp.view.main.MainActivity
import java.util.Locale

private val Context.dataStoreLanguage: DataStore<Preferences> by preferencesDataStore(name = "language")

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var locale: Locale
    private lateinit var settingsViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewModel()
        setupLanguageButtons()
    }

    private fun setupToolbar() {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        val preferences = LanguagePreferences.getInstance(dataStoreLanguage)
        settingsViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(preferences)
        )[SettingViewModel::class.java]

        settingsViewModel.getLanguagePreference().observe(this) { language ->
            locale = language
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
        }
    }

    private fun setupLanguageButtons() {
        binding.btnEngland.setOnClickListener {
            changeLanguage("en", "US")
        }

        binding.btnIndonesia.setOnClickListener {
            changeLanguage("in", "ID")
        }
    }

    private fun changeLanguage(language: String, country: String) {
        locale = Locale(language, country)
        settingsViewModel.setLanguagePreference(language)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
        Toast.makeText(this, R.string.switch_language, Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}