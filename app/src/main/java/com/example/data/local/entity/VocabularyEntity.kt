package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary")
data class VocabularyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val word: String,
    val phoneticDevanagari: String, // e.g., "कॉम्फर्टेबल"
    val phoneticIpa: String = "",   // e.g., "/ˈkʌmf.tə.bəl/"
    val meaningHindi: String,       // e.g., "आरामदायक, सुखद"
    val partOfSpeech: String = "Noun", // Noun, Verb, Adj, etc.
    val exampleSentenceEn: String,
    val exampleSentenceHi: String,
    val category: String = "Daily Use", // Daily Use, Workplace, Interview, Travel, Idioms
    val repetitionCount: Int = 0,
    val intervalDays: Int = 1,
    val easeFactor: Float = 2.5f,
    val nextReviewTimestamp: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val isMastered: Boolean = false
)
