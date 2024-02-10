package com.karpovich.findmykidstest.app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.ui.viewmodels.MainViewModel
import com.karpovich.findmykidstest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
}