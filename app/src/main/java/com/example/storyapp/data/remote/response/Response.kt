package com.example.storyapp.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RegisterRequest(
    var name: String,
    var email: String,
    var password: String
)

data class RegisterResponse(
    var error: Boolean,
    var message: String
)

data class LoginRequest(
    var email: String,
    var password: String
)

data class LoginResponse(
    var error: Boolean,
    var message: String,
    var loginResult: LoginResult
)

data class LoginResult(
    var userId: String,
    var name: String,
    var token: String
)

data class StoryResponse(
    var error: Boolean,
    var message: String,
    var listStory: List<ListStory>
)

@Parcelize
data class ListStory(
    var id: String,
    var name: String,
    var description: String,
    var photoUrl: String,
    var createdAt: String,
    var lat: Double,
    var lon: Double
) : Parcelable

data class UploadResponse(
    var error: Boolean,
    var message: String
)