package com.example.data.repository

import com.example.data.local.dao.SavedPhraseDao
import com.example.data.local.entity.SavedPhraseEntity
import com.example.data.model.SeedPhrases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

/**
 * Repository providing offline phrasebook caching, category filtering,
 * and user phrase management.
 */
class SavedPhraseRepository(private val savedPhraseDao: SavedPhraseDao) {

    val allPhrases: Flow<List<SavedPhraseEntity>> = savedPhraseDao.getAllSavedPhrases()
    val favoritePhrases: Flow<List<SavedPhraseEntity>> = savedPhraseDao.getFavoritePhrases()
    val phraseCount: Flow<Int> = savedPhraseDao.getPhraseCount()

    fun getByCategory(category: String): Flow<List<SavedPhraseEntity>> {
        return if (category == "All") {
            savedPhraseDao.getAllSavedPhrases()
        } else {
            savedPhraseDao.getPhrasesByCategory(category)
        }
    }

    fun search(query: String): Flow<List<SavedPhraseEntity>> {
        return savedPhraseDao.searchPhrases(query.trim())
    }

    suspend fun checkAndSeedInitialPhrases() {
        val count = savedPhraseDao.getPhraseCount().firstOrNull() ?: 0
        if (count == 0) {
            savedPhraseDao.insertAll(SeedPhrases.initialPhrases)
        }
    }

    suspend fun savePhrase(
        englishPhrase: String,
        hindiTranslation: String,
        phoneticDevanagari: String,
        category: String = "Saved",
        situationContext: String = "",
        formalityLevel: String = "Polite"
    ): Long {
        val existing = savedPhraseDao.findByEnglish(englishPhrase.trim())
        return if (existing != null) {
            existing.id
        } else {
            val entity = SavedPhraseEntity(
                englishPhrase = englishPhrase.trim(),
                hindiTranslation = hindiTranslation.trim(),
                phoneticDevanagari = phoneticDevanagari.trim(),
                category = category,
                situationContext = situationContext.trim(),
                formalityLevel = formalityLevel,
                isFavorite = true
            )
            savedPhraseDao.insertPhrase(entity)
        }
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        savedPhraseDao.updateFavoriteStatus(id, isFavorite)
    }

    suspend fun recordPractice(id: Long) {
        savedPhraseDao.markPracticed(id)
    }

    suspend fun deletePhrase(id: Long) {
        savedPhraseDao.deleteById(id)
    }
}
