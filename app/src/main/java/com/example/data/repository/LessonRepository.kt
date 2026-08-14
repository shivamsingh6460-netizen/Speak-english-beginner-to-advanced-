package com.example.data.repository

import com.example.data.local.dao.ProgressDao
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.model.Lesson
import com.example.data.model.LessonsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonRepository(private val progressDao: ProgressDao) {

    val allLessons: List<Lesson> = LessonsData.allLessons

    val lessonProgressMap: Flow<Map<String, LessonProgressEntity>> =
        progressDao.getAllLessonProgress().map { list ->
            list.associateBy { it.lessonId }
        }

    fun getLessonById(id: String): Lesson? {
        return allLessons.find { it.id == id }
    }

    suspend fun markLessonCompleted(lessonId: String, score: Int, maxScore: Int) {
        progressDao.saveLessonProgress(
            LessonProgressEntity(
                lessonId = lessonId,
                isCompleted = true,
                quizScore = score,
                maxScore = maxScore,
                lastCompletedTimestamp = System.currentTimeMillis()
            )
        )
    }
}
