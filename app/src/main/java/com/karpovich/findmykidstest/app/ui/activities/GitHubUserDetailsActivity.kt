package com.karpovich.findmykidstest.app.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.data.AppRepository
import com.karpovich.findmykidstest.databinding.ActivityGitHubUserDetailsBinding
import com.karpovich.findmykidstest.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubUserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGitHubUserDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGitHubUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        parseIntent(intent)
    }
    private fun init() {
        setClickListeners()
    }
    private fun parseIntent(intent: Intent) {
        val login = intent.getStringExtra(EXTRA_LOGIN)
        Log.d("инт", "parseIntent: $login")
    }
    private fun setClickListeners() {
        binding.backButtonImageView.setOnClickListener {
            finish()
        }
    }
    companion object {

        private const val EXTRA_LOGIN = "extra_login"
        fun newIntent(context: Context, login: String): Intent {
            val intent = Intent(context, GitHubUserDetailsActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, login)
            return intent
        }
    }
}