package com.example.data.repository

import com.example.data.local.dao.DailyTaskDao
import com.example.data.local.entity.DailyTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DailyTaskRepository(private val dailyTaskDao: DailyTaskDao) {

    val allTasks: Flow<List<DailyTaskEntity>> = dailyTaskDao.getAllTasks()
    val pendingTasks: Flow<List<DailyTaskEntity>> = dailyTaskDao.getPendingTasks()
    val completedTasks: Flow<List<DailyTaskEntity>> = dailyTaskDao.getCompletedTasks()
    val totalTaskCount: Flow<Int> = dailyTaskDao.getTotalTaskCount()
    val completedTaskCount: Flow<Int> = dailyTaskDao.getCompletedTaskCount()

    suspend fun addTask(
        title: String,
        category: String = "English Practice",
        priority: String = "Medium",
        dueDate: String = "Today"
    ): Long = withContext(Dispatchers.IO) {
        val task = DailyTaskEntity(
            title = title.trim(),
            category = category,
            priority = priority,
            dueDate = dueDate,
            isCompleted = false,
            createdAt = System.currentTimeMillis()
        )
        dailyTaskDao.insertTask(task)
    }

    suspend fun toggleTaskCompletion(task: DailyTaskEntity) = withContext(Dispatchers.IO) {
        val newStatus = !task.isCompleted
        val completedAt = if (newStatus) System.currentTimeMillis() else null
        dailyTaskDao.toggleTaskStatus(task.id, newStatus, completedAt)
    }

    suspend fun deleteTask(task: DailyTaskEntity) = withContext(Dispatchers.IO) {
        dailyTaskDao.deleteTask(task)
    }

    suspend fun deleteTaskById(id: Long) = withContext(Dispatchers.IO) {
        dailyTaskDao.deleteTaskById(id)
    }

    suspend fun seedSampleTasksIfEmpty() = withContext(Dispatchers.IO) {
        val tasks = listOf(
            DailyTaskEntity(
                title = "Practice 10m spoken English with AI Tutor",
                category = "Speaking",
                priority = "High",
                dueDate = "Today"
            ),
            DailyTaskEntity(
                title = "Read 1 Chapter from Open Library ('The Gift of the Magi')",
                category = "Reading",
                priority = "High",
                dueDate = "Today"
            ),
            DailyTaskEntity(
                title = "Master 5 new vocabulary flashcards",
                category = "Vocabulary",
                priority = "Medium",
                dueDate = "Today"
            ),
            DailyTaskEntity(
                title = "Complete 'V vs W' pronunciation drill",
                category = "Speaking",
                priority = "Medium",
                dueDate = "Today"
            ),
            DailyTaskEntity(
                title = "Write a 3-sentence daily journal note in English",
                category = "English Practice",
                priority = "Low",
                dueDate = "Today"
            )
        )
        tasks.forEach { dailyTaskDao.insertTask(it) }
    }
}
