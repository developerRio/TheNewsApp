package com.originalstocks.thenewsapp.data.network

import com.originalstocks.thenewsapp.data.model.NewsDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestInterface {

    @GET("top-headlines")
    suspend fun getNewsHeadlinesAsync(
        @Query("country") country: String, // keeping constant as india (in)
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int, // 20 headlines per page
        @Query("page") pageNumber: Int, // init with index 1
    ): Response<NewsDataModel>

}