package com.karpovich.findmykidstest.app.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        setupRecycleView(login)
        setupScrollViewListener(login)
    }
    private fun setupViewModel(login: String) {
        val viewModelFactory = UserDetailsViewModelFactory(login, AppRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[UserDetailsViewModel::class.java]
    }
    private fun observeViewModel() {
        observeGitHubUserDetailsLoading()
        observeGitHubUserFollowersLoading()
        observeGitHubUserDetails()
        observeGitHubFollowers()
        observeErrorMessage()
    }
    private fun setupRecycleView(login: String) {
        val rvUserFollowers = binding.subsUserRecyclerView
        with(rvUserFollowers){
            gitHubUserAdapter = GitHubUserAdapter()
            adapter = gitHubUserAdapter
            val spanCount = 2
            val layoutManager = GridLayoutManager(this@UserDetailsActivity, spanCount)
            rvUserFollowers.layoutManager = layoutManager
        }
        onNestedScrollViewScrollEndReached(login)
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
    private fun observeGitHubUserDetailsLoading() {
        viewModel.isGitHubUserDetailsLoading.observe(this) {
            if (it) {
                hideViewsOnUserDetailsLoading()
            } else {
                showViewsAfterUserDetailsLoading()
            }
        }
    }
    private fun observeGitHubUserFollowersLoading() {
        viewModel.isGitHubUserFollowersLoading.observe(this) {
            if (it){
                binding.progressBarUserFollowers.visibility = View.VISIBLE
            } else {
                binding.progressBarUserFollowers.visibility = View.GONE
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

    private fun hideViewsOnUserDetailsLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cardView.visibility = View.INVISIBLE
    }

    private fun showViewsAfterUserDetailsLoading() {
        binding.cardView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
    private fun onNestedScrollViewScrollEndReached(login: String) {
        val nestedScrollView = binding.nestedScrollView
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY == nestedScrollView.getChildAt(0).measuredHeight - nestedScrollView.measuredHeight) {
                viewModel.loadGitHubUserFollowers(login)
            }
        }
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