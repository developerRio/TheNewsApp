package com.originalstocks.thenewsapp.data.paging

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.originalstocks.thenewsapp.data.cache.NewsDao
import com.originalstocks.thenewsapp.data.cache.NewsDataPrimaryKey
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel
import com.originalstocks.thenewsapp.data.network.RequestInterface
import com.originalstocks.thenewsapp.utils.API_KEY
import com.originalstocks.thenewsapp.utils.PAGE_SIZE
import com.originalstocks.thenewsapp.utils.STARTING_INDEX
import java.io.InvalidObjectException

private const val TAG = "NewsRemoteMediator"

@ExperimentalPagingApi
class NewsRemoteMediator(
    private val newsDao: NewsDao,
    private val requestInterface: RequestInterface,
    private val initialPage: Int = STARTING_INDEX
) : RemoteMediator<Int, NewsHeadlineModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsHeadlineModel>
    ): MediatorResult {

        return try {
            val pageNumber = when (loadType) {
                LoadType.APPEND -> {
                    val remoteKey = getLastRemoteKey(state)
                        ?: throw InvalidObjectException("InvalidObjectException Occurred")
                    remoteKey.next ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    val remoteKey = getClosestRemoteKeys(state)
                    remoteKey?.next?.plus(1) ?: initialPage
                }
            }

            /** creating request */
            val response = requestInterface.getNewsHeadlinesAsync(
                country = "in", // setting india as default news source
                apiKey = API_KEY,
                pageSize = PAGE_SIZE,
                pageNumber = pageNumber
            )

            val endOfPagination = response.body()?.articles?.size!! < state.config.pageSize
            Log.i(
                TAG,
                "load_page_number_ev = $pageNumber end_of_paging condition = ${response.body()?.articles?.size} < ${state.config.pageSize} "
            )

            if (response.isSuccessful) {
                response.body()?.let { newsDataModel ->
                    // initially flushing data on refresh
                    if (loadType == LoadType.REFRESH) {
                        newsDao.deleteAllNews()
                        newsDao.deleteAllRemoteKeys()
                    }

                    // Condition for maintaining page numbers as user scrolls back-n-forth
                    val prev = if (pageNumber == initialPage) null else pageNumber - 1
                    val next = if (endOfPagination) null else pageNumber + 1
                    Log.i(
                        TAG,
                        "load_previous_page initially = $pageNumber previously = $prev next_page = $next isEndOfPagination $endOfPagination"
                    )

                    // Storing primary keys data to paginate for next & previous pages
                    val newsList = response.body()?.articles?.map {
                        Log.i(
                            TAG,
                            "getting_response_previous_page ${it.id} $prev next_page = $next"
                        )
                        NewsDataPrimaryKey(it.id, prev, next)
                    }

                    // caching requested data from network request (firstly remote keys)
                    if (newsList != null) {
                        newsDao.insertAllRemoteKeys(newsList)
                    }

                    // caching requested data from network request (then actual data of newsModel)
                    newsDataModel.articles?.let { newsData -> newsDao.insertNewsData(newsData) }

                }
                MediatorResult.Success(endOfPagination)
            } else {
                MediatorResult.Success(endOfPaginationReached = true)
            }

        } catch (e: NetworkErrorException) {
            Log.e(TAG, "load_MediatorResult_exception NETWORK ISSUE = ${e.message}")
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "load_MediatorResult_exception = ${e.message}")
            MediatorResult.Error(e)
        }

    }

    private suspend fun getClosestRemoteKeys(state: PagingState<Int, NewsHeadlineModel>): NewsDataPrimaryKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                newsDao.getAllRemoteKey(it.id)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, NewsHeadlineModel>): NewsDataPrimaryKey? {
        return state.lastItemOrNull()?.let {
            newsDao.getAllRemoteKey(it.id)
        }
    }

}