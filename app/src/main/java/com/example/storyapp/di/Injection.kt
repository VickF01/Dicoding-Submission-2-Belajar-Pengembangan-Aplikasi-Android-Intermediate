package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.paging.StoryRepository
import com.example.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}