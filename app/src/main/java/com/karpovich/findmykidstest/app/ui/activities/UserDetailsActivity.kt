package com.karpovich.findmykidstest.app.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.data.AppRepository
import com.karpovich.findmykidstest.app.ui.adapters.GitHubUserAdapter
import com.karpovich.findmykidstest.app.ui.viewmodels.UserDetailsViewModel
import com.karpovich.findmykidstest.app.ui.viewmodels.UserDetailsViewModelFactory
import com.karpovich.findmykidstest.databinding.ActivityGitHubUserDetailsBinding

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGitHubUserDetailsBinding
    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var gitHubUserAdapter: GitHubUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGitHubUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init(parseIntentLogin(intent))
    }

    private fun init(login: String) {
        setupViewModel(login)
        setClickListeners()
        observeViewModel()
        setupRecycleView()
        setupScrollViewListener(login)
    }
    private fun setupViewModel(login: String) {
        val viewModelFactory = UserDetailsViewModelFactory(login, AppRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[UserDetailsViewModel::class.java]
    }
    private fun observeViewModel() {
        observeGitHubUserLoading()
        observeGitHubUserDetails()
        observeGitHubFollowers()
        observeErrorMessage()
    }
    private fun setupRecycleView() {
        val rvUserFollowers = binding.subsUserRecyclerView
        with(rvUserFollowers){
            gitHubUserAdapter = GitHubUserAdapter()
            adapter = gitHubUserAdapter
            val spanCount = 2
            val layoutManager = GridLayoutManager(this@UserDetailsActivity, spanCount)
            rvUserFollowers.layoutManager = layoutManager
        }
    }
    private fun parseIntentLogin(intent: Intent): String {
        val login = intent.getStringExtra(EXTRA_LOGIN).toString()
        binding.userLoginTextView.text = login
        return login
    }
    private fun setClickListeners() {
        binding.backButtonImageView.setOnClickListener {
            finish()
        }
    }
    private fun observeGitHubUserDetails() {
        viewModel.gitHubUserDetails.observe(this){
            if (it!=null){
                binding.userLoginTextView.text = it.login
                Glide.with(this)
                    .load(it.avatarUrl)
                    .into(binding.userAvatarImageView)
                binding.userCompanyTextView.text = it.company
                binding.userBioTextView.text = it.bio
                binding.userEmailTextView.text = it.email
                binding.userBlogTextView.text = it.blog
                binding.userLocationTextView.text = it.location
            }
        }
    }
    private fun observeGitHubUserLoading() {
        viewModel.isGitHubUserLoading.observe(this) { isLoading ->
            if (isLoading) {
                hideViewsOnLoading()
            } else {
                showViewsAfterLoading()
            }
        }
    }
    private fun observeGitHubFollowers() {
        viewModel.gitHubFollowers.observe(this){
            if (it!=null){
                gitHubUserAdapter.submitList(it)
            }
        }
    }
    private fun observeErrorMessage() {
        viewModel.errorMessage.observe(this){
            if (it){
                hideViewsOnLoading()
                binding.progressBar.visibility = View.INVISIBLE
                binding.errorMessageTextView.visibility = View.VISIBLE
            }
        }
    }
    private fun setupScrollViewListener(login: String) {
        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrollY = binding.nestedScrollView.scrollY
            val followersTextViewY = binding.followersTextView.y

            if (scrollY > followersTextViewY) {
                binding.userLoginTextView.text = getString(R.string.subs)
            } else {
                binding.userLoginTextView.text = login
            }
        }
    }

    private fun hideViewsOnLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.groupView.visibility = View.INVISIBLE
        binding.userLoginTextView.visibility = View.INVISIBLE
    }

    private fun showViewsAfterLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.groupView.visibility = View.VISIBLE
        binding.userLoginTextView.visibility = View.VISIBLE
    }
    companion object {

        private const val EXTRA_LOGIN = "extra_login"
        fun newIntent(context: Context, login: String): Intent {
            val intent = Intent(context, UserDetailsActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, login)
            return intent
        }
    }
}