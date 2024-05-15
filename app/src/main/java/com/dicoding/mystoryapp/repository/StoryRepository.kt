package com.dicoding.mystoryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystoryapp.data.remote.ApiService
import com.dicoding.mystoryapp.data.remote.ErrorResponse
import com.google.gson.Gson
import com.dicoding.mystoryapp.Result
import com.dicoding.mystoryapp.data.local.StoryDatabase
import com.dicoding.mystoryapp.data.local.StoryRemoteMediator
import com.dicoding.mystoryapp.data.preference.TokenPreferences
import com.dicoding.mystoryapp.data.remote.ApiConfig
import com.dicoding.mystoryapp.data.remote.LoginResult
import com.dicoding.mystoryapp.data.remote.StoryListItem
import com.dicoding.mystoryapp.util.reduceFileImage
import com.dicoding.mystoryapp.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File


class StoryRepository(
    private var apiService: ApiService,
    private val pref: TokenPreferences,
    private val storyDatabase: StoryDatabase
) {
    fun register(name: String, email: String, password: String): LiveData<Result<ErrorResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val error =
                    Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(error.message.toString()))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun login(email: String, password: String): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val response = apiService.login(email, password)
                val user = response.loginResult
                if (user != null) {
                    emit(Result.Success(user))
                } else {
                    emit(Result.Error("There was an error"))
                }
            } catch (e: HttpException) {
                val error =
                    Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(error.message.toString()))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }


    fun uploadImage(
        getFile: File?,
        description: String,
        lat: Float? = null,
        lon: Float? = null
    ): LiveData<Result<ErrorResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                if (getFile != null) {
                    val file = getFile.reduceFileImage()
                    val desc = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    val response = apiService.uploadImage(imageMultipart, desc, lat, lon)
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error("Error: No File Attached!"))
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                emit(Result.Error(errorBody.message.toString()))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getStoriesWithLocation(): LiveData<Result<List<StoryListItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                pref.getToken().first()
            }
            apiService = ApiConfig.getApiConfig(token.toString())
            val response = apiService.getStoriesWithLocation()
            val result = response.storyListItem
            emit(Result.Success(result))
        } catch (e: HttpException) {
            val error =
                Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
            emit(Result.Error(error.message.toString()))
        } catch (e: Exception) {
            Log.e("StoryRepository", "getAllStory: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStoriesPaging(coroutineScope: CoroutineScope): LiveData<Result<PagingData<StoryListItem>>> =
        liveData {
            emit(Result.Loading)
            try {
                val token = runBlocking {
                    pref.getToken().first()
                }
                apiService = ApiConfig.getApiConfig(token.toString())
                val pager = Pager(
                    config = PagingConfig(pageSize = 5),
                    remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
                    pagingSourceFactory = {
                        storyDatabase.storyDao().getAllStory()
                    }
                )
                val pagingDataFlow = pager.flow.cachedIn(coroutineScope)
                pagingDataFlow.collect {
                    emit(Result.Success(it))
                }
            } catch (e: HttpException) {
                val error =
                    Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(error.message.toString()))
            } catch (e: Exception) {
                Log.e("StoryRepository", "getAllStory: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            pref: TokenPreferences,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref, storyDatabase).also { instance = it }
            }
    }
}