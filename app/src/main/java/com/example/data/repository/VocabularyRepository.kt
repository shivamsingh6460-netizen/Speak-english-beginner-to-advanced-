package com.example.data.repository

import com.example.data.local.dao.VocabularyDao
import com.example.data.local.entity.VocabularyEntity
import com.example.data.model.SeedVocabulary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class VocabularyRepository(private val vocabularyDao: VocabularyDao) {

    val allVocabulary: Flow<List<VocabularyEntity>> = vocabularyDao.getAllVocabulary()
    val vocabularyCount: Flow<Int> = vocabularyDao.getVocabularyCount()
    val masteredCount: Flow<Int> = vocabularyDao.getMasteredCount()

    fun getByCategory(category: String): Flow<List<VocabularyEntity>> {
        return if (category == "All") {
            vocabularyDao.getAllVocabulary()
        } else {
            vocabularyDao.getVocabularyByCategory(category)
        }
    }

    suspend fun checkAndSeedInitialData() {
        val count = vocabularyDao.getVocabularyCount().firstOrNull() ?: 0
        if (count == 0) {
            vocabularyDao.insertAll(SeedVocabulary.initialVocabulary)
        }
    }

    suspend fun addVocabulary(
        word: String,
        phoneticDevanagari: String,
        phoneticIpa: String = "",
        meaningHindi: String,
        partOfSpeech: String = "Noun",
        exampleSentenceEn: String = "",
        exampleSentenceHi: String = "",
        category: String = "Daily Use"
    ): Long {
        val existing = vocabularyDao.findByWord(word.trim())
        return if (existing != null) {
            existing.id
        } else {
            val entity = VocabularyEntity(
                word = word.trim(),
                phoneticDevanagari = phoneticDevanagari.trim(),
                phoneticIpa = phoneticIpa.trim(),
                meaningHindi = meaningHindi.trim(),
                partOfSpeech = partOfSpeech.trim(),
                exampleSentenceEn = exampleSentenceEn.trim(),
                exampleSentenceHi = exampleSentenceHi.trim(),
                category = category
            )
            vocabularyDao.insertVocabulary(entity)
        }
    }

    /**
     * Anki / SM-2 spaced repetition calculation:
     * Rating: 1 = Again, 2 = Hard, 3 = Good, 4 = Easy
     */
    suspend fun reviewFlashcard(item: VocabularyEntity, rating: Int) {
        var newInterval: Int
        var newRepetition: Int = item.repetitionCount
        var newEaseFactor: Float = item.easeFactor
        var isMastered: Boolean = item.isMastered

        when (rating) {
            1 -> { // Again
                newRepetition = 0
                newInterval = 1
                newEaseFactor = (newEaseFactor - 0.2f).coerceAtLeast(1.3f)
                isMastered = false
            }
            2 -> { // Hard
                newRepetition += 1
                newInterval = if (newRepetition == 1) 1 else (item.intervalDays * 1.2f).toInt()
                newEaseFactor = (newEaseFactor - 0.15f).coerceAtLeast(1.3f)
            }
            3 -> { // Good
                newRepetition += 1
                newInterval = when (newRepetition) {
                    1 -> 1
                    2 -> 3
                    else -> (item.intervalDays * newEaseFactor).toInt()
                }
                if (newRepetition >= 4) isMastered = true
            }
            4 -> { // Easy
                newRepetition += 1
                newEaseFactor += 0.15f
                newInterval = when (newRepetition) {
                    1 -> 3
                    2 -> 6
                    else -> (item.intervalDays * newEaseFactor * 1.3f).toInt()
                }
                if (newRepetition >= 3) isMastered = true
            }
            else -> {
                newInterval = 1
            }
        }

        val nextTimestamp = System.currentTimeMillis() + (newInterval * 24L * 60L * 60L * 1000L)
        val updated = item.copy(
            repetitionCount = newRepetition,
            intervalDays = newInterval,
            easeFactor = newEaseFactor,
            nextReviewTimestamp = nextTimestamp,
            isMastered = isMastered
        )
        vocabularyDao.updateVocabulary(updated)
    }

    suspend fun deleteVocabulary(id: Long) {
        vocabularyDao.deleteById(id)
    }
}
