package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.BookProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookProgressDao {

    @Query("SELECT * FROM book_progress ORDER BY lastReadAt DESC")
    fun getAllProgress(): Flow<List<BookProgressEntity>>

    @Query("SELECT * FROM book_progress WHERE bookId = :bookId LIMIT 1")
    suspend fun getProgressForBook(bookId: String): BookProgressEntity?

    @Query("SELECT * FROM book_progress WHERE isBookmarked = 1 ORDER BY lastReadAt DESC")
    fun getBookmarkedBooks(): Flow<List<BookProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: BookProgressEntity)

    @Query("UPDATE book_progress SET isBookmarked = :isBookmarked WHERE bookId = :bookId")
    suspend fun toggleBookmark(bookId: String, isBookmarked: Boolean)
}
