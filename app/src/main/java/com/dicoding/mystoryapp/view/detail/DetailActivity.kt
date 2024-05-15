package com.dicoding.mystoryapp.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.mystoryapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val photoUrl = intent.getStringExtra("photoUrl")

        binding.tvDetailName.text = name
        binding.tvDetailDescription.text = description
        Glide.with(this).load(photoUrl).into(binding.ivDetailPhoto)
    }
}