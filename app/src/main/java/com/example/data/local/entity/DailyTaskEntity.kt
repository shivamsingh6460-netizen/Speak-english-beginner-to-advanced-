package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_tasks")
data class DailyTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String = "English Practice", // "English Practice", "Speaking", "Reading", "Work", "Personal"
    val priority: String = "Medium", // "High", "Medium", "Low"
    val isCompleted: Boolean = false,
    val dueDate: String = "", // e.g. "Today", "Daily", "Tomorrow"
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
