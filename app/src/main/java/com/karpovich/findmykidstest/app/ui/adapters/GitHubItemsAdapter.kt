package com.karpovich.findmykidstest.app.ui.adapters

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.data.network.entities.GitHubItemEntity
import com.karpovich.findmykidstest.app.ui.utilities.GitHubItemDiffCallback

class GitHubItemsAdapter : ListAdapter<GitHubItemEntity, GitHubItemViewHolder>(GitHubItemDiffCallback()) {

    var onGitUserClickListener: ((GitHubItemEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubItemViewHolder {
        val layout = R.layout.user_item
        val view = LayoutInflater.from(parent.context).inflate(layout,parent,false)
        return GitHubItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: GitHubItemViewHolder, position: Int) {
        val gitHubUser = getItem(position)
        viewHolder.view.setOnClickListener{
            onGitUserClickListener?.invoke(gitHubUser)
        }
        viewHolder.userLoginTextView.text = gitHubUser.login
        val img = gitHubUser.avatarUrl
        Glide.with(viewHolder.itemView)
            .load(img)
            .into(viewHolder.userIconImageView)
        viewHolder.userIconImageView
    }

}