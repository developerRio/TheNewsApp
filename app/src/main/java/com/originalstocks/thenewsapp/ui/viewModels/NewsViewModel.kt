package com.originalstocks.thenewsapp.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.originalstocks.thenewsapp.data.cache.NewsDao
import com.originalstocks.thenewsapp.data.network.RequestInterface
import com.originalstocks.thenewsapp.data.paging.NewsRemoteMediator
import com.originalstocks.thenewsapp.utils.PAGE_SIZE
import com.originalstocks.thenewsapp.utils.STARTING_INDEX

class NewsViewModel
@ViewModelInject
constructor(private val newsDao: NewsDao, private val requestInterface: RequestInterface): ViewModel() {

    @ExperimentalPagingApi
    val pager = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        remoteMediator = NewsRemoteMediator(
            newsDao = newsDao,
            requestInterface = requestInterface,
            initialPage = STARTING_INDEX
        )
    ) {
        newsDao.getAllNewsData()
    }.flow
}