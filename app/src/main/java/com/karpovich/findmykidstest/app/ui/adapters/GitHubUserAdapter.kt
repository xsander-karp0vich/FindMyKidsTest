package com.karpovich.findmykidstest.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import com.karpovich.findmykidstest.app.ui.utilities.GitHubUserDiffCallback

class GitHubUserAdapter : ListAdapter<GitHubUserEntity, GitHubUserViewHolder>(GitHubUserDiffCallback()) {

    var onGitUserClickListener: ((GitHubUserEntity) -> Unit)? = null
    var onEndReachedListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubUserViewHolder {
        val layout = R.layout.user_item
        val view = LayoutInflater.from(parent.context).inflate(layout,parent,false)
        return GitHubUserViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: GitHubUserViewHolder, position: Int) {
        val gitHubUser = getItem(position)
        viewHolder.view.setOnClickListener{
            onGitUserClickListener?.invoke(gitHubUser)
        }
        viewHolder.userLoginTextView.text = gitHubUser.login
        val img = gitHubUser.avatarUrl
        Glide.with(viewHolder.itemView)
            .load(img)
            .placeholder(R.drawable.oriental_cat)
            .into(viewHolder.userIconImageView)
        viewHolder.userIconImageView
        viewHolder.countOfFollowersTextView.text = gitHubUser.followers.toString()
        viewHolder.countOfRepoTextView.text = gitHubUser.publicRepos.toString()

        if (position == itemCount - ONE) {
            onEndReachedListener?.invoke()
        }
    }
    private companion object {
        private const val ONE = 1
    }
}