package com.dicoding.mystoryapp.view.splashcreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.preference.dataStore
import com.dicoding.mystoryapp.view.main.MainActivity
import com.dicoding.mystoryapp.view.onboarding.OnBoardingActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val content = findViewById<View>(android.R.id.content)
        @Suppress("UNUSED_EXPRESSION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            content.viewTreeObserver.addOnDrawListener { false }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val userLogin = runBlocking {
                TokenPreferences.getInstance(this@SplashActivity.dataStore).getLoginStatus().first()
            }
            if (userLogin == true) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, OnBoardingActivity::class.java))
                finish()
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, SPLASH_TIME_OUT)
    }

    companion object {
        private const val SPLASH_TIME_OUT: Long = 3500
    }
}