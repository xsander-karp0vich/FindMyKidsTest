package com.karpovich.findmykidstest.app.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.data.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubUserDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_hub_user_details)
        parseIntent(intent)
    }
    private fun parseIntent(intent: Intent) {
        val login = intent.getStringExtra(EXTRA_LOGIN)
        Log.d("инт", "parseIntent: $login")
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