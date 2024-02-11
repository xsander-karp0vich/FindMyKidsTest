package com.karpovich.findmykidstest.app.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karpovich.findmykidstest.R

class GitHubItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val userLoginTextView = view.findViewById<TextView>(R.id.userLoginTextView)
    val userIconImageView = view.findViewById<ImageView>(R.id.userIconImageView)
    val countOfSubsTextView = view.findViewById<TextView>(R.id.countOfSubsTextView)
    val countOfRepoTextView = view.findViewById<TextView>(R.id.countOfRepoTextView)
}