package com.example.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.LoginRequest
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.viewmodel.LoginPreferences
import com.example.storyapp.viewmodel.LoginViewModel
import com.example.storyapp.viewmodel.MainViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        playAnimation()

        binding.registerText.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (!isDataOk()) {
                Toast.makeText(this, getString(R.string.data_incorrect), Toast.LENGTH_SHORT).show()
            } else {
                val account = LoginRequest(email, password)
                mainViewModel.loginAccount(account)
            }
        }

        mainViewModel.message.observe(this) {
            when(it) {
                "Unauthorized" -> {
                    Toast.makeText(this, getString(R.string.email_password_incorrect), Toast.LENGTH_SHORT).show()
                }
                "success" -> {
                    val pref = LoginPreferences.getInstance(dataStore)
                    val loginViewModel = ViewModelProvider(this@MainActivity, ViewModelFactory(pref))[LoginViewModel::class.java]
                    mainViewModel.token.observe(this) { token ->
                        loginViewModel.setToken(token)
                        loginViewModel.setLoggedIn(true)
                        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_Y, -10f, 10f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val loginText = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailLoginEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.passwordLoginEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)
        val register = ObjectAnimator.ofFloat(binding.registerText, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playTogether(email, password)
            playSequentially(loginText, loginButton, register)
            start()
        }
    }

    private fun isDataOk() = binding.edLoginEmail.isValid && binding.edLoginPassword.isValid

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}