package com.example.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.response.LoginRequest
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainViewModel : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginAccount(account: LoginRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(account)
        client.enqueue(object : Callback, retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>,response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _message.value = responseBody?.message
                    _token.value = responseBody?.loginResult?.token
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}