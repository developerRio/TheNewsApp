package com.originalstocks.thenewsapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsDataModel(
    var status: String?,
    val articles: List<NewsHeadlineModel>?
): Parcelable

@Parcelize
@Entity
data class NewsHeadlineModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var author: String?,
    var title: String?,
    var description: String?,
    var urlToImage: String?,
    var content: String?,
    var publishedAt: String?,
): Parcelable
