package com.example.storyapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.R
import com.example.storyapp.viewmodel.LoginPreferences
import com.example.storyapp.viewmodel.LoginViewModel
import com.example.storyapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    companion object {
        const val delay: Long = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(delay)
            withContext(Dispatchers.Main) {

                val pref = LoginPreferences.getInstance(dataStore)
                val loginViewModel = ViewModelProvider(this@SplashScreen, ViewModelFactory(pref))[LoginViewModel::class.java]

                loginViewModel.isLoggedIn().observe(this@SplashScreen) {
                    if (it) {
                        startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}