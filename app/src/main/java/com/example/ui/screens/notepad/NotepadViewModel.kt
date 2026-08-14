package com.example.ui.screens.notepad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.DailyTaskEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.model.GrammarCorrectionResult
import com.example.data.remote.GeminiClient
import com.example.data.repository.DailyTaskRepository
import com.example.data.repository.NoteRepository
import com.example.data.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NotepadTab(val title: String, val titleHindi: String) {
    NOTES("Notepad", "नोट्स और डायरी"),
    DAILY_TASKS("Daily Tasks", "दैनिक कार्य")
}

data class NoteEditState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val colorHex: String = "#FFF9C4",
    val category: String = "English Notes",
    val isPinned: Boolean = false,
    val isOpen: Boolean = false
)

class NotepadViewModel(
    private val noteRepository: NoteRepository,
    private val dailyTaskRepository: DailyTaskRepository,
    private val progressRepository: ProgressRepository,
    private val geminiClient: GeminiClient
) : ViewModel() {

    private val _currentTab = MutableStateFlow(NotepadTab.NOTES)
    val currentTab: StateFlow<NotepadTab> = _currentTab.asStateFlow()

    // Notes State
    private val _noteSearchQuery = MutableStateFlow("")
    val noteSearchQuery: StateFlow<String> = _noteSearchQuery.asStateFlow()

    private val _selectedNoteCategory = MutableStateFlow("All")
    val selectedNoteCategory: StateFlow<String> = _selectedNoteCategory.asStateFlow()

    val allNotes: StateFlow<List<NoteEntity>> = noteRepository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredNotes: StateFlow<List<NoteEntity>> = combine(
        allNotes,
        _noteSearchQuery,
        _selectedNoteCategory
    ) { notes, query, category ->
        notes.filter { note ->
            val matchesCategory = (category == "All") || note.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                note.title.contains(query, ignoreCase = true) ||
                note.content.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _editNoteState = MutableStateFlow<NoteEditState?>(null)
    val editNoteState: StateFlow<NoteEditState?> = _editNoteState.asStateFlow()

    private val _isPolishingNote = MutableStateFlow(false)
    val isPolishingNote: StateFlow<Boolean> = _isPolishingNote.asStateFlow()

    private val _grammarFeedback = MutableStateFlow<GrammarCorrectionResult?>(null)
    val grammarFeedback: StateFlow<GrammarCorrectionResult?> = _grammarFeedback.asStateFlow()

    // Daily Tasks State
    val allTasks: StateFlow<List<DailyTaskEntity>> = dailyTaskRepository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingTasks: StateFlow<List<DailyTaskEntity>> = dailyTaskRepository.pendingTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedTasks: StateFlow<List<DailyTaskEntity>> = dailyTaskRepository.completedTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _taskFilter = MutableStateFlow("All") // "All", "Pending", "Completed"
    val taskFilter: StateFlow<String> = _taskFilter.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository.seedSampleNotesIfEmpty()
            dailyTaskRepository.seedSampleTasksIfEmpty()
        }
    }

    fun selectTab(tab: NotepadTab) {
        _currentTab.value = tab
    }

    fun onNoteSearchChanged(query: String) {
        _noteSearchQuery.value = query
    }

    fun onNoteCategoryChanged(category: String) {
        _selectedNoteCategory.value = category
    }

    fun openNewNote() {
        _grammarFeedback.value = null
        _editNoteState.value = NoteEditState(
            id = 0,
            title = "",
            content = "",
            colorHex = "#FFF9C4",
            category = "English Notes",
            isOpen = true
        )
    }

    fun openEditNote(note: NoteEntity) {
        _grammarFeedback.value = null
        _editNoteState.value = NoteEditState(
            id = note.id,
            title = note.title,
            content = note.content,
            colorHex = note.colorHex,
            category = note.category,
            isPinned = note.isPinned,
            isOpen = true
        )
    }

    fun updateEditNote(
        title: String? = null,
        content: String? = null,
        colorHex: String? = null,
        category: String? = null,
        isPinned: Boolean? = null
    ) {
        val current = _editNoteState.value ?: return
        _editNoteState.value = current.copy(
            title = title ?: current.title,
            content = content ?: current.content,
            colorHex = colorHex ?: current.colorHex,
            category = category ?: current.category,
            isPinned = isPinned ?: current.isPinned
        )
    }

    fun saveCurrentNote() {
        val current = _editNoteState.value ?: return
        if (current.title.isBlank() && current.content.isBlank()) {
            _editNoteState.value = null
            return
        }

        viewModelScope.launch {
            val titleToSave = current.title.ifBlank { "Untitled Note" }
            noteRepository.saveNote(
                id = current.id,
                title = titleToSave,
                content = current.content,
                colorHex = current.colorHex,
                category = current.category,
                isPinned = current.isPinned
            )
            progressRepository.addXp(5)
            _editNoteState.value = null
            _grammarFeedback.value = null
        }
    }

    fun deleteCurrentNote() {
        val current = _editNoteState.value ?: return
        if (current.id != 0L) {
            viewModelScope.launch {
                noteRepository.deleteNoteById(current.id)
            }
        }
        _editNoteState.value = null
        _grammarFeedback.value = null
    }

    fun closeNoteEditor() {
        _editNoteState.value = null
        _grammarFeedback.value = null
    }

    fun togglePin(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.togglePin(note.id, !note.isPinned)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun polishNoteWithAI() {
        val current = _editNoteState.value ?: return
        val textToPolish = "${current.title}\n${current.content}".trim()
        if (textToPolish.isBlank()) return

        viewModelScope.launch {
            _isPolishingNote.value = true
            try {
                val result = geminiClient.checkGrammar(textToPolish)
                _grammarFeedback.value = result
            } catch (e: Exception) {
                // Ignore error gracefully
            } finally {
                _isPolishingNote.value = false
            }
        }
    }

    fun applyAiCorrection(correctedText: String) {
        val lines = correctedText.split("\n", limit = 2)
        val newTitle = lines.firstOrNull() ?: ""
        val newContent = if (lines.size > 1) lines[1] else ""
        updateEditNote(title = newTitle, content = newContent)
        _grammarFeedback.value = null
    }

    fun dismissGrammarFeedback() {
        _grammarFeedback.value = null
    }

    // Daily Tasks
    fun setTaskFilter(filter: String) {
        _taskFilter.value = filter
    }

    fun addTask(
        title: String,
        category: String = "English Practice",
        priority: String = "Medium",
        dueDate: String = "Today"
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            dailyTaskRepository.addTask(
                title = title.trim(),
                category = category,
                priority = priority,
                dueDate = dueDate
            )
            progressRepository.addXp(5)
        }
    }

    fun toggleTask(task: DailyTaskEntity) {
        viewModelScope.launch {
            dailyTaskRepository.toggleTaskCompletion(task)
            if (!task.isCompleted) {
                progressRepository.addXp(15)
            }
        }
    }

    fun deleteTask(task: DailyTaskEntity) {
        viewModelScope.launch {
            dailyTaskRepository.deleteTask(task)
        }
    }
}

class NotepadViewModelFactory(
    private val noteRepository: NoteRepository,
    private val dailyTaskRepository: DailyTaskRepository,
    private val progressRepository: ProgressRepository,
    private val geminiClient: GeminiClient
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotepadViewModel(noteRepository, dailyTaskRepository, progressRepository, geminiClient) as T
    }
}
