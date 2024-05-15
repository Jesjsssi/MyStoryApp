package com.dicoding.mystoryapp.view.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.ViewModelFactory
import com.dicoding.mystoryapp.databinding.ActivitySignupBinding
import com.dicoding.mystoryapp.Result
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.preference.dataStore
import com.dicoding.mystoryapp.view.welcome.WelcomeActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.progressBar.visibility = View.GONE

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                TokenPreferences.getInstance(dataStore)
            )
        viewModel = ViewModelProvider(this, factory)[SignupViewModel::class.java]

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.register(name, email, password).observe(this) { response ->
                when (response) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        AlertDialog.Builder(this).apply {
                            setTitle("Success!")
                            setMessage(getString(R.string.signup_success))
                            setPositiveButton("OK") { _, _ ->
                                startActivity(
                                    Intent(
                                        this@SignupActivity,
                                        WelcomeActivity::class.java
                                    )
                                )
                            }
                            create()
                            show()
                        }
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        AlertDialog.Builder(this).apply {
                            setTitle("Error!")
                            setMessage(response.error)
                            setPositiveButton("Try Again") { _, _ -> }
                            create()
                            show()
                        }
                    }

                }
            }
        }
    }
}