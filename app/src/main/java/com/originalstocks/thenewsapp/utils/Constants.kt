package com.originalstocks.thenewsapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel

const val BASE_URL = "https://newsapi.org/v2/"
const val API_KEY = "57d09c5f2a7f4f849719301700435587"

const val STARTING_INDEX = 1
const val PAGE_SIZE = 20

object DiffCallback : DiffUtil.ItemCallback<NewsHeadlineModel>() {
    override fun areItemsTheSame(
        oldItem: NewsHeadlineModel,
        newItem: NewsHeadlineModel
    ): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(
        oldItem: NewsHeadlineModel,
        newItem: NewsHeadlineModel
    ): Boolean {
        return oldItem == newItem
    }
}

fun showSnackbar(view: View, message: String, length: Int) {
    when (length) {
        0 -> {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }
        1 -> {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
        2 -> {
            Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show()
        }
    }
}

enum class ApiStatus {
    LOADING, SUCCESS, ERROR
}
