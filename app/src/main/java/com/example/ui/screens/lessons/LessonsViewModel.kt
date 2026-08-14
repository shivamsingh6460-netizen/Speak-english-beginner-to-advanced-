package com.example.ui.screens.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.model.Lesson
import com.example.data.model.QuizQuestion
import com.example.data.repository.LessonRepository
import com.example.data.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LessonsViewModel(
    private val lessonRepository: LessonRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val allLessons: List<Lesson> = lessonRepository.allLessons

    val lessonProgressMap: StateFlow<Map<String, LessonProgressEntity>> =
        lessonRepository.lessonProgressMap
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    // Quiz & Challenge state inside lesson
    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedAnswerIndex = MutableStateFlow<Int?>(null)
    val selectedAnswerIndex: StateFlow<Int?> = _selectedAnswerIndex.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _isQuizCompleted = MutableStateFlow(false)
    val isQuizCompleted: StateFlow<Boolean> = _isQuizCompleted.asStateFlow()

    fun selectLesson(lesson: Lesson) {
        _selectedLesson.value = lesson
        _currentQuizIndex.value = 0
        _selectedAnswerIndex.value = null
        _quizScore.value = 0
        _isQuizCompleted.value = false
    }

    fun closeLessonDetail() {
        _selectedLesson.value = null
    }

    fun selectAnswer(index: Int) {
        if (_selectedAnswerIndex.value != null) return
        _selectedAnswerIndex.value = index

        val lesson = _selectedLesson.value ?: return
        val currentQ = lesson.quiz.getOrNull(_currentQuizIndex.value) ?: return

        if (index == currentQ.correctIndex) {
            _quizScore.value += 1
        }
    }

    fun nextQuizQuestion() {
        val lesson = _selectedLesson.value ?: return
        if (_currentQuizIndex.value < lesson.quiz.size - 1) {
            _currentQuizIndex.value += 1
            _selectedAnswerIndex.value = null
        } else {
            _isQuizCompleted.value = true
            // Save progress
            viewModelScope.launch {
                lessonRepository.markLessonCompleted(
                    lessonId = lesson.id,
                    score = _quizScore.value,
                    maxScore = lesson.quiz.size
                )
                progressRepository.addSpeakingTime(60)
            }
        }
    }
}

class LessonsViewModelFactory(
    private val lessonRepository: LessonRepository,
    private val progressRepository: ProgressRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LessonsViewModel(lessonRepository, progressRepository) as T
    }
}
