package com.example.data.repository

import com.example.data.local.dao.ProgressDao
import com.example.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ProgressRepository(private val progressDao: ProgressDao) {

    val userProgress: Flow<UserProgressEntity?> = progressDao.getUserProgress()

    suspend fun ensureInitialized() {
        val current = progressDao.getUserProgressOnce()
        if (current == null) {
            val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            progressDao.insertOrUpdateUserProgress(
                UserProgressEntity(
                    id = 1,
                    streakDays = 3,
                    lastActiveDayOfYear = todayDay,
                    totalSpeakingSeconds = 180,
                    wordsLearnedCount = 10,
                    fluencyScore = 62,
                    weakAreaTips = "Articles (A/An/The), 'V' vs 'W' lip positioning"
                )
            )
        } else {
            // Check streak
            val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            if (current.lastActiveDayOfYear != todayDay) {
                val isConsecutive = (todayDay - current.lastActiveDayOfYear) == 1 || (todayDay == 1 && current.lastActiveDayOfYear >= 365)
                val newStreak = if (isConsecutive) current.streakDays + 1 else if (todayDay == current.lastActiveDayOfYear) current.streakDays else 1
                progressDao.insertOrUpdateUserProgress(
                    current.copy(
                        streakDays = newStreak,
                        lastActiveDayOfYear = todayDay
                    )
                )
            }
        }
    }

    suspend fun addXp(xp: Int) {
        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        val newFluency = (current.fluencyScore + (xp / 10)).coerceAtMost(99)
        progressDao.insertOrUpdateUserProgress(
            current.copy(
                fluencyScore = newFluency
            )
        )
    }

    suspend fun addSpeakingTime(seconds: Int) {
        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        val newFluency = (current.fluencyScore + (seconds / 40)).coerceAtMost(99)
        progressDao.insertOrUpdateUserProgress(
            current.copy(
                totalSpeakingSeconds = current.totalSpeakingSeconds + seconds,
                fluencyScore = newFluency
            )
        )
    }

    suspend fun incrementWordsLearned() {
        val current = progressDao.getUserProgressOnce() ?: UserProgressEntity()
        progressDao.insertOrUpdateUserProgress(
            current.copy(
                wordsLearnedCount = current.wordsLearnedCount + 1,
                fluencyScore = (current.fluencyScore + 1).coerceAtMost(99)
            )
        )
    }
}
