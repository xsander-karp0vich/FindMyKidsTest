package com.karpovich.findmykidstest.app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.karpovich.findmykidstest.app.ui.adapters.GitHubItemsAdapter
import com.karpovich.findmykidstest.app.ui.viewmodels.MainViewModel
import com.karpovich.findmykidstest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var gitHubItemsAdapter: GitHubItemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init() {
        setupRecycleView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observeViewModel()
    }
    private fun observeViewModel() {
        viewModel.isGitHubUsersLoading.observe(this){
            setupProgressBar(it)
        }
        viewModel.gitHubUsers.observe(this){
            Log.d("таг", "observeViewModel: $it")
            if (it != null) {
                gitHubItemsAdapter.submitList(it)
            }
        }
    }
    private fun setupRecycleView() {
        val rvGitHubUsers = binding.usersRecycleView
        with(rvGitHubUsers){
            gitHubItemsAdapter = GitHubItemsAdapter()
            adapter = gitHubItemsAdapter
            val spanCount = 2
            val layoutManager = GridLayoutManager(this@MainActivity, spanCount)
            rvGitHubUsers.layoutManager = layoutManager
            setupClickListener()
            setupOnEndOfListReachedListener()
        }
    }
    private fun setupClickListener() {
        gitHubItemsAdapter.onGitUserClickListener = {
            Log.d("таг", "setupClickListener: ${it.login}")
            launchToGitHubUserDetailsActivity(it.login)
        }
    }
    private fun setupOnEndOfListReachedListener() {
        gitHubItemsAdapter.onEndOfListReachedListener = {
            //вспомнил что пагинация не нужна и обрадовался
        }
    }
    private fun launchToGitHubUserDetailsActivity(login:String) {
        val intent = GitHubUserDetailsActivity.newIntent(this, login)
        startActivity(intent)
    }
    private fun setupProgressBar(boolean: Boolean) {
        val progressBar = binding.progressBar
        if (boolean){
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            binding.users.visibility = View.VISIBLE
            binding.usersRecycleView.visibility = View.VISIBLE
        }
    }
}