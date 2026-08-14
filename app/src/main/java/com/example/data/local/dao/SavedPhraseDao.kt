package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.SavedPhraseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local persistent phrase storage and offline phrasebook operations.
 */
@Dao
interface SavedPhraseDao {

    @Query("SELECT * FROM saved_phrases ORDER BY createdAt DESC")
    fun getAllSavedPhrases(): Flow<List<SavedPhraseEntity>>

    @Query("SELECT * FROM saved_phrases WHERE category = :category ORDER BY createdAt DESC")
    fun getPhrasesByCategory(category: String): Flow<List<SavedPhraseEntity>>

    @Query("SELECT * FROM saved_phrases WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoritePhrases(): Flow<List<SavedPhraseEntity>>

    @Query("SELECT * FROM saved_phrases WHERE englishPhrase LIKE '%' || :query || '%' OR hindiTranslation LIKE '%' || :query || '%'")
    fun searchPhrases(query: String): Flow<List<SavedPhraseEntity>>

    @Query("SELECT COUNT(*) FROM saved_phrases")
    fun getPhraseCount(): Flow<Int>

    @Query("SELECT * FROM saved_phrases WHERE englishPhrase = :englishPhrase LIMIT 1")
    suspend fun findByEnglish(englishPhrase: String): SavedPhraseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhrase(phrase: SavedPhraseEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(phrases: List<SavedPhraseEntity>)

    @Update
    suspend fun updatePhrase(phrase: SavedPhraseEntity)

    @Delete
    suspend fun deletePhrase(phrase: SavedPhraseEntity)

    @Query("DELETE FROM saved_phrases WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE saved_phrases SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Query("UPDATE saved_phrases SET practiceCount = practiceCount + 1, lastPracticedTimestamp = :timestamp WHERE id = :id")
    suspend fun markPracticed(id: Long, timestamp: Long = System.currentTimeMillis())
}
