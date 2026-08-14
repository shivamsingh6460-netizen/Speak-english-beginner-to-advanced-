package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity for storing useful everyday English phrases with Hindi translations,
 * Devanagari pronunciation phonetics, situational context, and offline learning progress.
 */
@Entity(tableName = "saved_phrases")
data class SavedPhraseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val englishPhrase: String,
    val hindiTranslation: String,
    val phoneticDevanagari: String, // e.g., "कुड यू प्लीज हेल्प मी?"
    val category: String = "Daily Essentials", // Daily Essentials, Workplace & Office, Travel & Transport, Shopping & Market, Social & Greetings, Saved
    val situationContext: String = "", // e.g. "Used when asking for assistance politely"
    val formalityLevel: String = "Polite", // Casual, Polite, Formal
    val audioKey: String = "",
    val isFavorite: Boolean = true,
    val practiceCount: Int = 0,
    val lastPracticedTimestamp: Long = 0L,
    val createdAt: Long = System.currentTimeMillis()
)
