package com.dicoding.mystoryapp.data.remote

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @field:SerializedName("userId")
    val error: Boolean? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)