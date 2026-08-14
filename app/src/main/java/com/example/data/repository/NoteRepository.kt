package com.example.data.repository

import com.example.data.local.dao.NoteDao
import com.example.data.local.entity.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)

    fun getNotesByCategory(category: String): Flow<List<NoteEntity>> = noteDao.getNotesByCategory(category)

    suspend fun getNoteById(id: Long): NoteEntity? = withContext(Dispatchers.IO) {
        noteDao.getNoteById(id)
    }

    suspend fun saveNote(
        id: Long = 0,
        title: String,
        content: String,
        colorHex: String = "#FFF9C4",
        category: String = "English Notes",
        isPinned: Boolean = false
    ): Long = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val note = NoteEntity(
            id = id,
            title = title.trim(),
            content = content.trim(),
            colorHex = colorHex,
            category = category,
            isPinned = isPinned,
            createdAt = if (id == 0L) now else now,
            updatedAt = now
        )
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteNoteById(id: Long) = withContext(Dispatchers.IO) {
        noteDao.deleteNoteById(id)
    }

    suspend fun togglePin(id: Long, isPinned: Boolean) = withContext(Dispatchers.IO) {
        noteDao.togglePin(id, isPinned)
    }

    suspend fun seedSampleNotesIfEmpty() = withContext(Dispatchers.IO) {
        // Add helpful starter notes for English learners if table is empty
        val sample1 = NoteEntity(
            title = "Golden Rules of Spoken English",
            content = "1. Don't worry about making mistakes; confidence is key!\n2. Think in English instead of translating from Hindi in your head.\n3. Learn complete phrases and idioms, not isolated words.\n4. Practice speaking aloud 10-15 minutes every day.",
            colorHex = "#FFF9C4",
            category = "Grammar",
            isPinned = true
        )
        val sample2 = NoteEntity(
            title = "My Daily Vocabulary Journal",
            content = "Words I learned today from SpeakEasy:\n• Ubiquitous (सर्वव्यापी) - Smartphones are ubiquitous.\n• Diligent (परिश्रमी) - A diligent student succeeds.\n• Meticulous (अतिसावधान) - He did a meticulous review.",
            colorHex = "#E1BEE7",
            category = "Vocab",
            isPinned = false
        )
        noteDao.insertNote(sample1)
        noteDao.insertNote(sample2)
    }
}
