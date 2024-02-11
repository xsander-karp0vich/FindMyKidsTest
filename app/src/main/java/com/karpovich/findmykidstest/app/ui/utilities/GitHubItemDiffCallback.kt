package com.karpovich.findmykidstest.app.ui.utilities

import androidx.recyclerview.widget.DiffUtil
import com.karpovich.findmykidstest.app.data.network.entities.GitHubItemEntity

class GitHubItemDiffCallback: DiffUtil.ItemCallback<GitHubItemEntity>() {
    override fun areItemsTheSame(oldItem: GitHubItemEntity, newItem: GitHubItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GitHubItemEntity, newItem: GitHubItemEntity): Boolean {
        return oldItem == newItem
    }
}