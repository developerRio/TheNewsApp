package com.originalstocks.thenewsapp.ui.adapters

import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel

interface ItemClickListener {
    fun fetchItem(data: NewsHeadlineModel)
}