package com.example.data.model

data class BookChapter(
    val chapterNumber: Int,
    val title: String,
    val titleHindi: String = "",
    val content: String,
    val keyVocabulary: List<BookWordGlossary> = emptyList(),
    val summaryHindi: String = ""
)

data class BookWordGlossary(
    val word: String,
    val meaningHindi: String,
    val phoneticHindi: String = "",
    val exampleSentence: String = ""
)

data class OpenBook(
    val id: String,
    val title: String,
    val titleHindi: String,
    val author: String,
    val authorHindi: String = "",
    val year: String,
    val coverEmoji: String,
    val category: String, // "Classics", "Short Stories", "Folk Tales", "Inspiration", "Adventure", "Graded Readers"
    val difficultyLevel: String, // "Beginner (आसान)", "Intermediate (मध्यम)", "Advanced (उन्नत)"
    val estimatedReadTimeMinutes: Int,
    val descriptionEn: String,
    val descriptionHindi: String,
    val chapters: List<BookChapter>,
    val gutenbergOrOpenLibraryUrl: String = "",
    val tags: List<String> = emptyList()
)
