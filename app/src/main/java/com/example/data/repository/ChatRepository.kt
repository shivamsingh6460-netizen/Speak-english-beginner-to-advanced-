package com.example.data.repository

import com.example.data.local.dao.ChatDao
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.model.TutorReply
import com.example.data.remote.GeminiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ChatRepository(
    private val chatDao: ChatDao,
    private val geminiClient: GeminiClient,
    private val progressRepository: ProgressRepository
) {
    fun getMessages(topicId: String): Flow<List<ChatMessageEntity>> {
        return chatDao.getMessagesByTopic(topicId)
    }

    suspend fun sendMessage(
        topicId: String,
        userText: String,
        topicName: String,
        difficultyLevel: String
    ): TutorReply {
        // 1. Insert user message
        chatDao.insertMessage(
            ChatMessageEntity(
                topicId = topicId,
                sender = "user",
                text = userText
            )
        )

        // 2. Build short history
        val recentMessages = chatDao.getMessagesByTopic(topicId).firstOrNull() ?: emptyList()
        val historyPairs = recentMessages.takeLast(6).map { it.sender to it.text }

        // 3. Call AI Tutor
        val tutorReply = geminiClient.chatWithTutor(
            history = historyPairs,
            userMessage = userText,
            topic = topicName,
            level = difficultyLevel
        )

        // 4. Save tutor reply to database
        chatDao.insertMessage(
            ChatMessageEntity(
                topicId = topicId,
                sender = "tutor",
                text = tutorReply.englishReply,
                correctionHindi = tutorReply.correctionHindi,
                correctedText = tutorReply.correctedSentence
            )
        )

        // 5. Track active practice seconds and update progress
        progressRepository.addSpeakingTime(25)

        return tutorReply
    }

    suspend fun clearHistory(topicId: String) {
        chatDao.clearTopicHistory(topicId)
    }
}
