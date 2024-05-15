package com.dicoding.mystoryapp.view.login

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
import com.dicoding.mystoryapp.Result
import com.dicoding.mystoryapp.ViewModelFactory
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.preference.dataStore
import com.dicoding.mystoryapp.databinding.ActivityLoginBinding
import com.dicoding.mystoryapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.progressBar.visibility = View.GONE

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                TokenPreferences.getInstance(dataStore)
            )
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

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
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password).observe(this) { response ->
                when (response) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = response.data
                        viewModel.saveState(data.token.toString())
                        AlertDialog.Builder(this).apply {
                            setTitle("Success!")
                            setMessage(getString(R.string.login_success))
                            setPositiveButton(getString(R.string.continue_dialog)) { _, _ ->
                                Intent(this@LoginActivity, MainActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(this)
                                }
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
                            setPositiveButton(getString(R.string.continue_dialog)) { _, _ -> }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }
}