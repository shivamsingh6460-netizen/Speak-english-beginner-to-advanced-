package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WordBreakdownItem(
    val word: String,
    val devanagariPhonetic: String, // e.g. "कम्फर्टेबल"
    val ipaPhonetic: String = "",
    val hindiMeaning: String,
    val partOfSpeech: String,
    val exampleSentenceEn: String = "",
    val exampleSentenceHi: String = ""
)

@JsonClass(generateAdapter = true)
data class TranslationResult(
    val translatedText: String,
    val transliteration: String = "",
    val explanationHindi: String = "",
    val words: List<WordBreakdownItem> = emptyList(),
    val grammarTip: String = ""
)

@JsonClass(generateAdapter = true)
data class TutorReply(
    val englishReply: String,
    val detectedUserMistake: String? = null,
    val correctionHindi: String? = null,
    val correctedSentence: String? = null,
    val encouragementHindi: String? = null
)

@JsonClass(generateAdapter = true)
data class PronunciationAnalysis(
    val targetText: String,
    val spokenText: String,
    val accuracyScore: Int, // 0 - 100
    val feedbackHindi: String,
    val phoneticTip: String,
    val problematicSounds: List<String> = emptyList(),
    val isGoodEnough: Boolean = true
)

@JsonClass(generateAdapter = true)
data class GrammarCorrectionResult(
    val originalText: String,
    val correctedText: String,
    val isGrammaticallyCorrect: Boolean = false,
    val explanationHindi: String = "",
    val grammarRulesApplied: List<String> = emptyList(),
    val alternativeBetterPhrasing: String = "",
    val toneFormality: String = "Polite"
)
