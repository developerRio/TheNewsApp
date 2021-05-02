package com.originalstocks.thenewsapp.data.cache

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.originalstocks.thenewsapp.data.model.NewsHeadlineModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NewsDatabaseTest : TestCase() {
    private lateinit var db: NewsDatabase
    private lateinit var dao: NewsDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()

        dao = db.getNewsDao()
    }

    @Test
    fun writeReadNewsData() = runBlocking{
        val headlineModel = NewsHeadlineModel(
            1,
            "Random author",
            "Random title",
            "Random Description",
            "Random image URL",
            "Some Random Content",
            "11 april 2021"
        )

        dao.insertNewsSingle(headlineModel)

        val headline = dao.getAllNewsListTest()
        assertThat(headline.contains(headlineModel)).isTrue()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}