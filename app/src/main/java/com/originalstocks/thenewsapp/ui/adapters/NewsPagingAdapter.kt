package com.originalstocks.thenewsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.originalstocks.thenewsapp.R
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel
import com.originalstocks.thenewsapp.databinding.NewsListItemsLayoutBinding
import com.originalstocks.thenewsapp.utils.DiffCallback

class NewsPagingAdapter(val itemClickListener: ItemClickListener): PagingDataAdapter<NewsHeadlineModel, NewsPagingAdapter.NewsViewHolder>(DiffCallback) {

    inner class NewsViewHolder(val binding: NewsListItemsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val data = getItem(position)

        holder.binding.apply {
            titleTextView.text = data?.title
            descriptionTextView.text = data?.description
            dateTextView.text = data?.publishedAt
            sourceTextView.text = data?.author
            Glide.with(root).load(data?.urlToImage).into(newsImageView)
        }

        holder.binding.newsItemView.setOnClickListener {
            if (data != null) {
                itemClickListener.fetchItem(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsListItemsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }
}