package com.dicoding.mystoryapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.mystoryapp.data.remote.StoryListItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryListItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryListItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}