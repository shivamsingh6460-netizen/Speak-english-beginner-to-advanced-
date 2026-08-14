package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val id: Int = 1,
    val streakDays: Int = 1,
    val lastActiveDayOfYear: Int = 0,
    val totalSpeakingSeconds: Int = 0,
    val wordsLearnedCount: Int = 0,
    val fluencyScore: Int = 54, // 0 - 100
    val weakAreaTips: String = "Past tense verb forms, Article usage ('a' vs 'the')"
)
