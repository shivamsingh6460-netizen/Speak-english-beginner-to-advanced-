package com.example.ui.screens.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.VocabularyEntity
import com.example.data.repository.ProgressRepository
import com.example.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VocabularyViewModel(
    private val vocabularyRepository: VocabularyRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isFlashcardMode = MutableStateFlow(false)
    val isFlashcardMode: StateFlow<Boolean> = _isFlashcardMode.asStateFlow()

    private val _currentFlashcardIndex = MutableStateFlow(0)
    val currentFlashcardIndex: StateFlow<Int> = _currentFlashcardIndex.asStateFlow()

    private val _showAddWordDialog = MutableStateFlow(false)
    val showAddWordDialog: StateFlow<Boolean> = _showAddWordDialog.asStateFlow()

    val categories = listOf("All", "Daily Use", "Workplace", "Travel", "Idioms")

    val vocabularyList: StateFlow<List<VocabularyEntity>> = combine(
        vocabularyRepository.allVocabulary,
        _selectedCategory,
        _searchQuery
    ) { all, category, query ->
        all.filter { item ->
            val matchCategory = category == "All" || item.category.equals(category, ignoreCase = true)
            val matchQuery = query.isBlank() ||
                    item.word.contains(query, ignoreCase = true) ||
                    item.meaningHindi.contains(query, ignoreCase = true) ||
                    item.phoneticDevanagari.contains(query, ignoreCase = true)
            matchCategory && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCount: StateFlow<Int> = vocabularyRepository.vocabularyCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val masteredCount: StateFlow<Int> = vocabularyRepository.masteredCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        _currentFlashcardIndex.value = 0
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _currentFlashcardIndex.value = 0
    }

    fun toggleFlashcardMode(enable: Boolean) {
        _isFlashcardMode.value = enable
        _currentFlashcardIndex.value = 0
    }

    fun setAddWordDialogVisible(visible: Boolean) {
        _showAddWordDialog.value = visible
    }

    fun rateFlashcard(item: VocabularyEntity, rating: Int) {
        viewModelScope.launch {
            vocabularyRepository.reviewFlashcard(item, rating)
            progressRepository.incrementWordsLearned()
            // advance to next card
            val currentList = vocabularyList.value
            if (_currentFlashcardIndex.value < currentList.size - 1) {
                _currentFlashcardIndex.value += 1
            } else {
                _currentFlashcardIndex.value = 0
            }
        }
    }

    fun addCustomWord(
        word: String,
        phoneticDevanagari: String,
        meaningHindi: String,
        partOfSpeech: String,
        exampleEn: String,
        exampleHi: String,
        category: String
    ) {
        viewModelScope.launch {
            vocabularyRepository.addVocabulary(
                word = word,
                phoneticDevanagari = phoneticDevanagari,
                meaningHindi = meaningHindi,
                partOfSpeech = partOfSpeech,
                exampleSentenceEn = exampleEn,
                exampleSentenceHi = exampleHi,
                category = category
            )
            progressRepository.incrementWordsLearned()
            _showAddWordDialog.value = false
        }
    }

    fun deleteWord(id: Long) {
        viewModelScope.launch {
            vocabularyRepository.deleteVocabulary(id)
        }
    }
}

class VocabularyViewModelFactory(
    private val vocabularyRepository: VocabularyRepository,
    private val progressRepository: ProgressRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VocabularyViewModel(vocabularyRepository, progressRepository) as T
    }
}
