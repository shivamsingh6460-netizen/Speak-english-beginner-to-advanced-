package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topicId: String = "general",
    val sender: String, // "user" or "tutor"
    val text: String,
    val correctionHindi: String? = null, // Gentle explanation in Hindi if user made a mistake
    val correctedText: String? = null,   // The grammatically correct English version
    val timestamp: Long = System.currentTimeMillis()
)
