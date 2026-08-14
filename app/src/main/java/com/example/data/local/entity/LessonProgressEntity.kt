package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey
    val lessonId: String,
    val isCompleted: Boolean = false,
    val quizScore: Int = 0,
    val maxScore: Int = 0,
    val lastCompletedTimestamp: Long = 0L
)
