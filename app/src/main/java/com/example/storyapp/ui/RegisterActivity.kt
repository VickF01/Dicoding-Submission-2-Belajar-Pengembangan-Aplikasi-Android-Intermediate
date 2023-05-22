package com.example.storyapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.RegisterRequest
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.loginText.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (!isDataOk()) {
                Toast.makeText(this, getString(R.string.data_incorrect), Toast.LENGTH_SHORT).show()
            } else {
                val account = RegisterRequest(name, email, password)
                registerViewModel.registerAccount(account)
            }

        }

        registerViewModel.message.observe(this) {
            when(it) {
                "User created" -> {
                    Toast.makeText(this, getString(R.string.user_created), Toast.LENGTH_SHORT).show()
                    finish()
                }
                "Bad Request" -> {
                    Toast.makeText(this, getString(R.string.email_taken), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isDataOk() = binding.edRegisterName.isValid && binding.edRegisterEmail.isValid && binding.edRegisterPassword.isValid

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}