package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String, // "hi" or "en"
    val targetLang: String, // "en" or "hi"
    val breakdownJson: String = "", // Serialized word-by-word analysis
    val timestamp: Long = System.currentTimeMillis()
)
