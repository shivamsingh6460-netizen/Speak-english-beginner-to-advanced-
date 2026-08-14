package com.example.ui.screens.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ConversationTopic(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val starterPrompt: String
)

class ConversationViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    val availableTopics = listOf(
        ConversationTopic("daily_life", "Daily Life & Routines", "दैनिक दिनचर्या", "Let's talk about your daily routine! What time do you usually wake up?"),
        ConversationTopic("job_interview", "Job Interview Prep", "जॉब इंटरव्यू की तैयारी", "Hello! Welcome to the interview. Could you please tell me about yourself and your background?"),
        ConversationTopic("travel", "Travel & Hotel Booking", "यात्रा और होटल", "Welcome to our hotel! How can I assist you with your booking today?"),
        ConversationTopic("restaurant", "Restaurant & Ordering", "रेस्टोरेंट में आर्डर करना", "Good evening! Welcome to our restaurant. Are you ready to order or would you like to see the menu?"),
        ConversationTopic("workplace", "Office & Business", "ऑफिस और मीटिंग्स", "Hi! Thanks for joining the project meeting. Could you give us a quick update on your task?"),
        ConversationTopic("casual_chat", "Hobbies & Friends", "शौक और बातचीत", "Hey! What do you like to do on weekends when you want to relax?")
    )

    private val _selectedTopicId = MutableStateFlow("daily_life")
    val selectedTopicId: StateFlow<String> = _selectedTopicId.asStateFlow()

    private val _selectedLevel = MutableStateFlow("Beginner") // Beginner, Intermediate, Advanced
    val selectedLevel: StateFlow<String> = _selectedLevel.asStateFlow()

    private val _speechSpeed = MutableStateFlow(0.85f) // 0.75f, 0.85f, 1.0f
    val speechSpeed: StateFlow<Float> = _speechSpeed.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    val currentMessages: StateFlow<List<ChatMessageEntity>> = _selectedTopicId
        .flatMapLatest { topicId ->
            chatRepository.getMessages(topicId)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Ensure default starter message if empty
        viewModelScope.launch {
            checkAndAddStarterMessage(_selectedTopicId.value)
        }
    }

    fun selectTopic(topicId: String) {
        _selectedTopicId.value = topicId
        viewModelScope.launch {
            checkAndAddStarterMessage(topicId)
        }
    }

    fun setLevel(level: String) {
        _selectedLevel.value = level
    }

    fun setSpeed(speed: Float) {
        _speechSpeed.value = speed
    }

    private suspend fun checkAndAddStarterMessage(topicId: String) {
        val topic = availableTopics.find { it.id == topicId } ?: return
        // We can let the user or tutor start
    }

    fun sendMessage(userText: String, onAiReply: (String) -> Unit = {}) {
        val text = userText.trim()
        if (text.isBlank()) return

        val topic = availableTopics.find { it.id == _selectedTopicId.value }
        val topicName = topic?.titleEn ?: "General Conversation"

        viewModelScope.launch {
            _isSending.value = true
            try {
                val reply = chatRepository.sendMessage(
                    topicId = _selectedTopicId.value,
                    userText = text,
                    topicName = topicName,
                    difficultyLevel = _selectedLevel.value
                )
                onAiReply(reply.englishReply)
            } finally {
                _isSending.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            chatRepository.clearHistory(_selectedTopicId.value)
        }
    }
}

class ConversationViewModelFactory(
    private val chatRepository: ChatRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConversationViewModel(chatRepository) as T
    }
}
