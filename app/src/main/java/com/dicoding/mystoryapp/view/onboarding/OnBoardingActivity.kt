package com.dicoding.mystoryapp.view.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityOnBoardingBinding
import com.dicoding.mystoryapp.view.welcome.WelcomeActivity

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Glide.with(this)
            .asGif()
            .load(R.drawable.logo_1)
            .into(binding.logoMenu1)
        Glide.with(this)
            .asGif()
            .load(R.drawable.logo_2)
            .into(binding.logoMenu2)
        Glide.with(this)
            .asGif()
            .load(R.drawable.logo_3)
            .into(binding.logoMenu3)
        Glide.with(this)
            .asGif()
            .load(R.drawable.logo_4)
            .into(binding.logoMenu4)

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
    }
}