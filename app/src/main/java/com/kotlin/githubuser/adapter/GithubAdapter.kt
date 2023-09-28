package com.kotlin.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.githubuser.data.model.Github
import com.kotlin.githubuser.databinding.ItemGithubBinding

class GithubAdapter(private val onclick: (Github) -> Unit) :
    ListAdapter<Github, GithubAdapter.GithubViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GithubViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        val github = getItem(position)
        holder.bind(github)
    }

    inner class GithubViewHolder(private val binding: ItemGithubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(github: Github) {
            binding.name.text = github.login
            Glide.with(itemView.context)
                .load(github.avatar_url)
                .into(binding.userAvatar)

            itemView.setOnClickListener { onclick(github) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Github>() {
            override fun areItemsTheSame(oldItem: Github, newItem: Github): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Github, newItem: Github): Boolean {
                return oldItem == newItem
            }

        }
    }
}