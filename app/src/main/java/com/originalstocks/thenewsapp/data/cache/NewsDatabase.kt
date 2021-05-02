package com.originalstocks.thenewsapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel

@Database(
    entities = [NewsHeadlineModel::class, NewsDataPrimaryKey::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}