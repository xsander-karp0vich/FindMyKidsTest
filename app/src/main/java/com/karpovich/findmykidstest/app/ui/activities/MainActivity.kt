package com.karpovich.findmykidstest.app.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.karpovich.findmykidstest.app.ui.adapters.GitHubUserAdapter
import com.karpovich.findmykidstest.app.ui.viewmodels.MainViewModel
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
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        observeViewModel()
    }
    private fun observeViewModel() {
        viewModel.isGitHubUsersLoading.observe(this){
            setupProgressBar(it)
        }
        viewModel.gitHubUsers.observe(this){
            if (it != null) {
                gitHubUserAdapter.submitList(it)
            }
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
        gitHubUserAdapter.onEndOfListReachedListener = {
            //вспомнил что пагинация не нужна и обрадовался
        }
    }
    private fun launchToGitHubUserDetailsActivity(login:String) {
        val intent = UserDetailsActivity.newIntent(this, login)
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