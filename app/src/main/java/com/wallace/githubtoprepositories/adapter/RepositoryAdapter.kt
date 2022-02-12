package com.wallace.githubtoprepositories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wallace.githubtoprepositories.databinding.RepositoryItemListBinding
import com.wallace.githubtoprepositories.model.Repository

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    inner class RepositoryViewHolder(val binding: RepositoryItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }
    }

    private val diff = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Repository>) = diff.submitList(list)


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
        val currentRepository = diff.currentList[position]

        holder.binding.apply {
            authorName.text = currentRepository.owner.login
            repoName.text = currentRepository.name
            qtdStars.text = currentRepository.numberOfStars.toString()
            qtdForks.text = currentRepository.numberOfForks.toString()

            Glide.with(authorPicture).load(currentRepository.owner.avatarURL).into(authorPicture)
        }
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }
}