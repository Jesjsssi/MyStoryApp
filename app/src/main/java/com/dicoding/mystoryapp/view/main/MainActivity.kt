package com.dicoding.mystoryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.Result
import com.dicoding.mystoryapp.ViewModelFactory
import com.dicoding.mystoryapp.adapter.LoadingStateAdapter
import com.dicoding.mystoryapp.adapter.StoryAdapter
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.preference.dataStore
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.setting.SettingActivity
import com.dicoding.mystoryapp.view.detail.DetailActivity
import com.dicoding.mystoryapp.view.maps.MapsActivity
import com.dicoding.mystoryapp.view.story.StoryActivity
import com.dicoding.mystoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                TokenPreferences.getInstance(dataStore)
            )
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupAction()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_option2 -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.are_your_sure))
                    setPositiveButton(getString(R.string.continue_dialog)) { _, _ ->
                        viewModel.logout()
                        val intent = Intent(context, WelcomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
                true
            }

            R.id.menu_option1 -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_option3 -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> false
        }

    }

    private fun setupAction() {
        val storyListAdapter = StoryAdapter()
        binding.rvStory.apply {
            adapter = storyListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = storyListAdapter
            storyListAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyListAdapter.retry()
                }
            )
        }

        storyListAdapter.onClick = { story ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", story.name)
            intent.putExtra("description", story.description)
            intent.putExtra("photoUrl", story.photoUrl)
            startActivity(intent)
        }
        viewModel.getStories.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = result.data
                    storyListAdapter.submitData(lifecycle, data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


