package com.example.data.repository

import com.example.data.local.dao.BookProgressDao
import com.example.data.local.entity.BookProgressEntity
import com.example.data.model.OpenBook
import com.example.data.model.OpenLibraryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LibraryRepository(
    private val bookProgressDao: BookProgressDao
) {

    val allBooks: List<OpenBook> = OpenLibraryData.books

    val readingHistory: Flow<List<BookProgressEntity>> = bookProgressDao.getAllProgress()
    val bookmarkedBooks: Flow<List<BookProgressEntity>> = bookProgressDao.getBookmarkedBooks()

    suspend fun getBookProgress(bookId: String): BookProgressEntity? = withContext(Dispatchers.IO) {
        bookProgressDao.getProgressForBook(bookId)
    }

    suspend fun saveBookProgress(
        book: OpenBook,
        currentChapter: Int,
        scrollProgress: Float = 0f,
        isBookmarked: Boolean = false
    ) = withContext(Dispatchers.IO) {
        val entity = BookProgressEntity(
            bookId = book.id,
            title = book.title,
            author = book.author,
            coverEmoji = book.coverEmoji,
            category = book.category,
            currentChapter = currentChapter,
            totalChapters = book.chapters.size,
            scrollProgress = scrollProgress,
            isBookmarked = isBookmarked,
            lastReadAt = System.currentTimeMillis()
        )
        bookProgressDao.saveProgress(entity)
    }

    suspend fun toggleBookmark(bookId: String, isBookmarked: Boolean) = withContext(Dispatchers.IO) {
        bookProgressDao.toggleBookmark(bookId, isBookmarked)
    }

    fun searchBooks(query: String): List<OpenBook> {
        if (query.isBlank()) return allBooks
        val clean = query.trim().lowercase()
        return allBooks.filter { book ->
            book.title.lowercase().contains(clean) ||
            book.titleHindi.contains(clean) ||
            book.author.lowercase().contains(clean) ||
            book.category.lowercase().contains(clean) ||
            book.tags.any { it.lowercase().contains(clean) }
        }
    }
}
