package com.matvey.newsapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    fun getAllNews(): Flow<List<News>>

    @Query("SELECT * FROM news_table WHERE id LIKE :id LIMIT 1")
    suspend fun getNewById(id: Int): News?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: News)

    @Query("DELETE FROM news_table")
    suspend fun deleteAll()
}