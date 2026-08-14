package com.example.ui.screens.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.TranslationHistoryEntity
import com.example.data.model.TranslationResult
import com.example.data.model.WordBreakdownItem
import com.example.data.repository.TranslationRepository
import com.example.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translationRepository: TranslationRepository,
    private val vocabularyRepository: VocabularyRepository
) : ViewModel() {

    private val _sourceLang = MutableStateFlow("hi") // "hi" or "en"
    val sourceLang: StateFlow<String> = _sourceLang.asStateFlow()

    private val _targetLang = MutableStateFlow("en")
    val targetLang: StateFlow<String> = _targetLang.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    private val _translationResult = MutableStateFlow<TranslationResult?>(null)
    val translationResult: StateFlow<TranslationResult?> = _translationResult.asStateFlow()

    val recentHistory: StateFlow<List<TranslationHistoryEntity>> =
        translationRepository.recentTranslations
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun swapLanguages() {
        val temp = _sourceLang.value
        _sourceLang.value = _targetLang.value
        _targetLang.value = temp
        _translationResult.value = null
    }

    fun translate() {
        val text = _inputText.value.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            _isTranslating.value = true
            try {
                val result = translationRepository.translate(
                    text = text,
                    sourceLang = _sourceLang.value,
                    targetLang = _targetLang.value
                )
                _translationResult.value = result
            } finally {
                _isTranslating.value = false
            }
        }
    }

    fun saveWordToVocabulary(item: WordBreakdownItem) {
        viewModelScope.launch {
            vocabularyRepository.addVocabulary(
                word = item.word,
                phoneticDevanagari = item.devanagariPhonetic,
                phoneticIpa = item.ipaPhonetic,
                meaningHindi = item.hindiMeaning,
                partOfSpeech = item.partOfSpeech,
                exampleSentenceEn = item.exampleSentenceEn,
                exampleSentenceHi = item.exampleSentenceHi,
                category = "Daily Use"
            )
        }
    }

    fun clearInput() {
        _inputText.value = ""
        _translationResult.value = null
    }
}

class TranslateViewModelFactory(
    private val translationRepository: TranslationRepository,
    private val vocabularyRepository: VocabularyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TranslateViewModel(translationRepository, vocabularyRepository) as T
    }
}
