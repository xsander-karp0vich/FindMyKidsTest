package com.karpovich.findmykidstest.app.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karpovich.findmykidstest.R

class GitHubUserViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val userLoginTextView: TextView = view.findViewById(R.id.userLoginTextView)
    val userIconImageView: ImageView = view.findViewById(R.id.userIconImageView)
    val countOfFollowersTextView: TextView = view.findViewById(R.id.countOfFollowersTextView)
    val countOfRepoTextView: TextView = view.findViewById(R.id.countOfRepoTextView)
}