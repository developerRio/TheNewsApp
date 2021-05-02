package com.originalstocks.thenewsapp.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsData(list: List<NewsHeadlineModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsSingle(newsHeadlineModel: NewsHeadlineModel)

    @Query("SELECT * FROM NewsHeadlineModel")
    fun getAllNewsData(): PagingSource<Int, NewsHeadlineModel> // paging net source

    /* only for testing call */
    @Query("SELECT * FROM NewsHeadlineModel")
    fun getAllNewsListTest(): List<NewsHeadlineModel>

    @Query("DELETE FROM NewsHeadlineModel")
    suspend fun deleteAllNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(list: List<NewsDataPrimaryKey>)

    @Query("SELECT * FROM NewsDataPrimaryKey WHERE id = :id")
    suspend fun getAllRemoteKey(id: Int): NewsDataPrimaryKey?

    @Query("DELETE FROM NewsDataPrimaryKey")
    suspend fun deleteAllRemoteKeys()

}