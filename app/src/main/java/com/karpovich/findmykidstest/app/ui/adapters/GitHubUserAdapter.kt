package com.karpovich.findmykidstest.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.karpovich.findmykidstest.R
import com.karpovich.findmykidstest.app.data.network.entities.GitHubUserEntity
import com.karpovich.findmykidstest.app.ui.utilities.GitHubItemDiffCallback

class GitHubUserAdapter : ListAdapter<GitHubUserEntity, GitHubUserViewHolder>(GitHubItemDiffCallback()) {

    var onGitUserClickListener: ((GitHubUserEntity) -> Unit)? = null
    var onEndOfListReachedListener: (() -> Unit)? = null

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