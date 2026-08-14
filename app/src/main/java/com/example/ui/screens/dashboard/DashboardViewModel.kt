package com.example.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.UserProgressEntity
import com.example.data.local.entity.VocabularyEntity
import com.example.data.repository.LessonRepository
import com.example.data.repository.ProgressRepository
import com.example.data.repository.VocabularyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val progressRepository: ProgressRepository,
    private val vocabularyRepository: VocabularyRepository,
    private val lessonRepository: LessonRepository
) : ViewModel() {

    val userProgress: StateFlow<UserProgressEntity?> = progressRepository.userProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val vocabularyCount: StateFlow<Int> = vocabularyRepository.vocabularyCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val masteredCount: StateFlow<Int> = vocabularyRepository.masteredCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allVocabulary: StateFlow<List<VocabularyEntity>> = vocabularyRepository.allVocabulary
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            progressRepository.ensureInitialized()
            vocabularyRepository.checkAndSeedInitialData()
        }
    }
}

class DashboardViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val vocabularyRepository: VocabularyRepository,
    private val lessonRepository: LessonRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(progressRepository, vocabularyRepository, lessonRepository) as T
    }
}
