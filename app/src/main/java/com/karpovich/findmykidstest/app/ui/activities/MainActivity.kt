package com.karpovich.findmykidstest.app.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.karpovich.findmykidstest.app.data.AppRepository
import com.karpovich.findmykidstest.app.ui.adapters.GitHubUserAdapter
import com.karpovich.findmykidstest.app.ui.viewmodels.MainViewModel
import com.karpovich.findmykidstest.app.ui.viewmodels.MainViewModelFactory
import com.karpovich.findmykidstest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var gitHubUserAdapter: GitHubUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init() {
        setupRecycleView()
        setupViewModel()
        observeViewModel()
    }
    private fun setupViewModel() {
        val viewModelFactory = MainViewModelFactory(AppRepository)
        viewModel = ViewModelProvider(this,viewModelFactory)[MainViewModel::class.java]
    }
    private fun observeViewModel() {
        observeGitHubUserLoading()
        observeGitHubUsers()
        observeErrorMessage()
    }
    private fun observeErrorMessage() {
        viewModel.errorMessage.observe(this){
            if (it){
                binding.progressBar.visibility = View.INVISIBLE
                binding.errorMessageTextView.visibility = View.VISIBLE
                binding.users.visibility = View.INVISIBLE
            }
        }
    }
    private fun observeGitHubUsers() {
        viewModel.gitHubUsers.observe(this){
            if (it != null) {
                gitHubUserAdapter.submitList(it)
            }
        }
    }
    private fun observeGitHubUserLoading() {
        viewModel.isGitHubUsersLoading.observe(this){
            observeGitHubUsersLoading(it)
        }
    }
    private fun setupRecycleView() {
        val rvGitHubUsers = binding.usersRecycleView
        with(rvGitHubUsers){
            gitHubUserAdapter = GitHubUserAdapter()
            adapter = gitHubUserAdapter
            val spanCount = 2
            val layoutManager = GridLayoutManager(this@MainActivity, spanCount)
            rvGitHubUsers.layoutManager = layoutManager
            setupClickListener()
            setupOnEndOfListReachedListener()
        }
    }
    private fun setupClickListener() {
        gitHubUserAdapter.onGitUserClickListener = {
            it.login?.let { it1 -> launchToGitHubUserDetailsActivity(it1) }
        }
    }
    private fun setupOnEndOfListReachedListener() {
        gitHubUserAdapter.onEndReachedListener = {
            viewModel.loadGitHubUsers()
        }
    }
    private fun launchToGitHubUserDetailsActivity(login:String) {
        UserDetailsActivity.newIntent(this, login).also {
            startActivity(it)
        }
    }
    private fun observeGitHubUsersLoading(boolean: Boolean) {
        if (boolean){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}