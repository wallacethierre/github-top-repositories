package com.wallace.githubtoprepositories.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wallace.githubtoprepositories.databinding.RepositoryItemListBinding
import com.wallace.githubtoprepositories.data.db.RepositoryEntry

class RepositoryAdapter : PagingDataAdapter<RepositoryEntry, RepositoryAdapter.RepositoryViewHolder>(diffCallback) {

    inner class RepositoryViewHolder(val binding: RepositoryItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<RepositoryEntry>() {
            override fun areItemsTheSame(oldItem: RepositoryEntry, newItem: RepositoryEntry): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RepositoryEntry, newItem: RepositoryEntry): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(
            RepositoryItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val currentRepository = getItem(position)

        holder.binding.apply {
            authorName.text = currentRepository?.ownerName
            repoName.text = currentRepository?.name
            qtdStars.text = currentRepository?.numberOfStars.toString()
            qtdForks.text = currentRepository?.numberOfForks.toString()

            Glide.with(authorPicture).load(currentRepository?.ownerPictureProfile).into(authorPicture)
        }
    }
}