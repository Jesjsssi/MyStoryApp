package com.dicoding.mystoryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.data.remote.StoryListItem
import com.dicoding.mystoryapp.databinding.StoryListBinding

class StoryAdapter : PagingDataAdapter<StoryListItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    var onClick: ((StoryListItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem!!, onClick)
    }

    class MyViewHolder(private val binding: StoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: StoryListItem, onClick: ((StoryListItem) -> Unit)?) {
            binding.tvItemName.text = currentItem.name
            binding.descriptionTv.text = currentItem.description
            binding.root.setOnClickListener {
                onClick?.invoke(currentItem)
            }

            Glide.with(binding.root)
                .load(currentItem.photoUrl)
                .error(R.drawable.login_pic)
                .into(binding.ivItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryListItem>() {
            override fun areItemsTheSame(oldItem: StoryListItem, newItem: StoryListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StoryListItem,
                newItem: StoryListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}