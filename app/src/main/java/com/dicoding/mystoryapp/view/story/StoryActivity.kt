package com.dicoding.mystoryapp.view.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.ViewModelFactory
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.preference.dataStore
import com.dicoding.mystoryapp.databinding.ActivityStoryBinding
import com.dicoding.mystoryapp.util.getImageUri
import com.dicoding.mystoryapp.Result
import com.dicoding.mystoryapp.util.uriToFile
import com.dicoding.mystoryapp.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private var currentImageUri: Uri? = null
    private var file: File? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.apply {
            progressBar.visibility = View.GONE

            btnGallery.setOnClickListener {
                startGallery()
            }

            btnCamera.setOnClickListener {
                startCamera()
            }

            buttonAdd.setOnClickListener {
                uploadImage()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                file = uriToFile(it!!, this)
            }
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageStory.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            Log.d("Camera", "Picture taken successfully")
            currentImageUri?.let {
                file = uriToFile(it, this)
            }
            showImage()
        } else {
            Log.d("Camera", "Failed to take picture")
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean -> }

    private fun requestLocationPermissionAndGetLocation(callback: (Location?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result
                callback(location)
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun uploadImage() {
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(
                this,
                TokenPreferences.getInstance(dataStore)
            )
        val viewModel: StoryViewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]

        val description = binding.edAddDescription.text.toString()

        if (binding.btnLocation.isChecked) {
            requestLocationPermissionAndGetLocation {
                if (it != null) {
                    viewModel.uploadImage(
                        file,
                        description,
                        it.latitude.toFloat(),
                        it.longitude.toFloat()
                    ).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Success Upload Image", Toast.LENGTH_SHORT)
                                        .show()
                                    Intent(this@StoryActivity, MainActivity::class.java).apply {
                                        flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(this)
                                    }
                                }

                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this, "Error Upload Image", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Location failed.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            viewModel.uploadImage(file, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Success Upload Image", Toast.LENGTH_SHORT).show()
                            Intent(this@StoryActivity, MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(this)
                            }
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Error Upload Image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}