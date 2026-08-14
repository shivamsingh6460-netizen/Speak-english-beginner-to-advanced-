package com.example.data.repository

import com.example.data.local.dao.TranslationHistoryDao
import com.example.data.local.entity.TranslationHistoryEntity
import com.example.data.model.TranslationResult
import com.example.data.remote.GeminiClient
import kotlinx.coroutines.flow.Flow

class TranslationRepository(
    private val translationDao: TranslationHistoryDao,
    private val geminiClient: GeminiClient,
    private val vocabularyRepository: VocabularyRepository
) {
    val recentTranslations: Flow<List<TranslationHistoryEntity>> =
        translationDao.getRecentTranslations()

    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): TranslationResult {
        val result = geminiClient.translateWithBreakdown(text, sourceLang, targetLang)

        // Save translation history
        translationDao.insertTranslation(
            TranslationHistoryEntity(
                sourceText = text,
                translatedText = result.translatedText,
                sourceLang = sourceLang,
                targetLang = targetLang
            )
        )

        return result
    }

    suspend fun clearHistory() {
        translationDao.clearAll()
    }
}
