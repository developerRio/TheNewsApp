package com.originalstocks.thenewsapp.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsDataPrimaryKey(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val previous: Int?,
    val next: Int?
)

