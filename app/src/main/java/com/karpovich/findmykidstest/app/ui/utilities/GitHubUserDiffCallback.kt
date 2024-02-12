package com.karpovich.findmykidstest.app.ui.utilities

import androidx.recyclerview.widget.DiffUtil
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity

class GitHubUserDiffCallback: DiffUtil.ItemCallback<GitHubUserEntity>() {
    override fun areItemsTheSame(oldItem: GitHubUserEntity, newItem: GitHubUserEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GitHubUserEntity, newItem: GitHubUserEntity): Boolean {
        return oldItem == newItem
    }

}