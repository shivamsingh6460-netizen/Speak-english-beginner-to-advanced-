package com.example.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.BookProgressEntity
import com.example.data.model.BookChapter
import com.example.data.model.BookWordGlossary
import com.example.data.model.OpenBook
import com.example.data.remote.GeminiClient
import com.example.data.repository.LibraryRepository
import com.example.data.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ReaderTheme(val displayName: String, val bgHex: Long, val textHex: Long) {
    PAPER("Paper (कागज़)", 0xFFFDFBF7, 0xFF2C2523),
    SEPIA("Sepia (हल्का भूरा)", 0xFFF4ECD8, 0xFF3E3129),
    NIGHT("Night (डार्क मोड)", 0xFF18181B, 0xFFE4E4E7),
    WHITE("Crisp White (सफेद)", 0xFFFFFFFF, 0xFF1E293B)
}

data class SelectedWordInfo(
    val word: String,
    val hindiMeaning: String,
    val definitionEn: String = "",
    val isLoading: Boolean = false
)

class LibraryViewModel(
    private val libraryRepository: LibraryRepository,
    private val progressRepository: ProgressRepository,
    private val geminiClient: GeminiClient
) : ViewModel() {

    val allBooks = libraryRepository.allBooks

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _activeBook = MutableStateFlow<OpenBook?>(null)
    val activeBook: StateFlow<OpenBook?> = _activeBook.asStateFlow()

    private val _currentChapterIndex = MutableStateFlow(0)
    val currentChapterIndex: StateFlow<Int> = _currentChapterIndex.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(17)
    val fontSizeSp: StateFlow<Int> = _fontSizeSp.asStateFlow()

    private val _readerTheme = MutableStateFlow(ReaderTheme.PAPER)
    val readerTheme: StateFlow<ReaderTheme> = _readerTheme.asStateFlow()

    private val _selectedWord = MutableStateFlow<SelectedWordInfo?>(null)
    val selectedWord: StateFlow<SelectedWordInfo?> = _selectedWord.asStateFlow()

    val readingHistory: StateFlow<List<BookProgressEntity>> = libraryRepository.readingHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: List<String> = listOf("All", "Classics", "Short Stories", "Folk Tales", "Inspiration", "Adventure", "Sci-Fi")

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun openBook(book: OpenBook, chapterIndex: Int = 0) {
        _activeBook.value = book
        _currentChapterIndex.value = chapterIndex.coerceIn(0, book.chapters.size - 1)
        viewModelScope.launch {
            val progress = libraryRepository.getBookProgress(book.id)
            val initialChapter = if (chapterIndex == 0 && progress != null) progress.currentChapter else chapterIndex
            _currentChapterIndex.value = initialChapter.coerceIn(0, book.chapters.size - 1)
            libraryRepository.saveBookProgress(book, _currentChapterIndex.value)
            progressRepository.addXp(10)
        }
    }

    fun closeReader() {
        _activeBook.value = null
        _selectedWord.value = null
    }

    fun nextChapter() {
        val book = _activeBook.value ?: return
        if (_currentChapterIndex.value < book.chapters.size - 1) {
            _currentChapterIndex.value += 1
            viewModelScope.launch {
                libraryRepository.saveBookProgress(book, _currentChapterIndex.value)
                progressRepository.addSpeakingTime(10)
            }
        }
    }

    fun previousChapter() {
        val book = _activeBook.value ?: return
        if (_currentChapterIndex.value > 0) {
            _currentChapterIndex.value -= 1
            viewModelScope.launch {
                libraryRepository.saveBookProgress(book, _currentChapterIndex.value)
            }
        }
    }

    fun increaseFontSize() {
        if (_fontSizeSp.value < 26) {
            _fontSizeSp.value += 2
        }
    }

    fun decreaseFontSize() {
        if (_fontSizeSp.value > 13) {
            _fontSizeSp.value -= 2
        }
    }

    fun setReaderTheme(theme: ReaderTheme) {
        _readerTheme.value = theme
    }

    fun lookupWordInBook(word: String, chapter: BookChapter) {
        val cleanWord = word.trim().replace(Regex("[^a-zA-Z]"), "")
        if (cleanWord.isBlank()) return

        val localGlossary = chapter.keyVocabulary.firstOrNull { it.word.equals(cleanWord, ignoreCase = true) }
        if (localGlossary != null) {
            _selectedWord.value = SelectedWordInfo(
                word = cleanWord,
                hindiMeaning = localGlossary.meaningHindi,
                definitionEn = localGlossary.exampleSentence
            )
            return
        }

        _selectedWord.value = SelectedWordInfo(word = cleanWord, hindiMeaning = "अर्थ लोड हो रहा है...", isLoading = true)

        viewModelScope.launch {
            try {
                val translation = geminiClient.translateText(cleanWord, "hi")
                _selectedWord.value = SelectedWordInfo(
                    word = cleanWord,
                    hindiMeaning = translation.translatedText,
                    definitionEn = translation.explanationHindi,
                    isLoading = false
                )
            } catch (e: Exception) {
                _selectedWord.value = SelectedWordInfo(
                    word = cleanWord,
                    hindiMeaning = "Meaning unavailable offline",
                    isLoading = false
                )
            }
        }
    }

    fun dismissWordLookup() {
        _selectedWord.value = null
    }

    fun getFilteredBooks(): List<OpenBook> {
        val query = _searchQuery.value.trim().lowercase()
        val cat = _selectedCategory.value

        return allBooks.filter { book ->
            val matchesCategory = (cat == "All") || book.category.equals(cat, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                book.title.lowercase().contains(query) ||
                book.titleHindi.contains(query) ||
                book.author.lowercase().contains(query) ||
                book.descriptionEn.lowercase().contains(query) ||
                book.tags.any { it.lowercase().contains(query) }
            matchesCategory && matchesQuery
        }
    }
}

class LibraryViewModelFactory(
    private val libraryRepository: LibraryRepository,
    private val progressRepository: ProgressRepository,
    private val geminiClient: GeminiClient
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LibraryViewModel(libraryRepository, progressRepository, geminiClient) as T
    }
}
