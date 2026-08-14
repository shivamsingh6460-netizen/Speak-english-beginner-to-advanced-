package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_progress")
data class BookProgressEntity(
    @PrimaryKey
    val bookId: String,
    val title: String,
    val author: String,
    val coverEmoji: String = "📖",
    val category: String,
    val currentChapter: Int = 0,
    val totalChapters: Int = 1,
    val scrollProgress: Float = 0f,
    val isBookmarked: Boolean = false,
    val lastReadAt: Long = System.currentTimeMillis()
)
