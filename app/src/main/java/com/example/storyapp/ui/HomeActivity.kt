package com.example.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.databinding.ActivityHomeBinding
import com.example.storyapp.viewmodel.*

@OptIn(ExperimentalPagingApi::class)
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactoryPaging(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.all_story)

        binding.rvStory.layoutManager = LinearLayoutManager(this@HomeActivity)

        val pref = LoginPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this@HomeActivity, ViewModelFactory(pref))[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this) {
            setStory(it)
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@HomeActivity, UploadActivity::class.java))
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                val pref = LoginPreferences.getInstance(dataStore)
                val loginViewModel = ViewModelProvider(this@HomeActivity, ViewModelFactory(pref))[LoginViewModel::class.java]
                loginViewModel.setLoggedIn(false)
                loginViewModel.setToken("")
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
                finish()
            }

            R.id.action_refresh -> {
                val pref = LoginPreferences.getInstance(dataStore)
                val loginViewModel = ViewModelProvider(this@HomeActivity, ViewModelFactory(pref))[LoginViewModel::class.java]
                loginViewModel.getToken().observe(this) {
                    setStory(it)
                }
            }

            R.id.action_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))

            R.id.action_maps -> startActivity(Intent(this@HomeActivity, MapsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    @ExperimentalPagingApi
    private fun setStory(token: String) {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.getAllStory(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}