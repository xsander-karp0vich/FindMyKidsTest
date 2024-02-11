package com.karpovich.findmykidstest.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.data.network.entities.GitHubItemEntity
import com.karpovich.findmykidstest.app.ui.utilities.GitHubItemDiffCallback

class GitHubItemsAdapter : ListAdapter<GitHubItemEntity, GitHubItemViewHolder>(GitHubItemDiffCallback()) {

    var onGitUserClickListener: ((GitHubItemEntity) -> Unit)? = null
    var onEndOfListReachedListener: (() -> Unit)? = null

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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    onEndOfListReachedListener?.invoke()
                }
            }
        })
    }
}