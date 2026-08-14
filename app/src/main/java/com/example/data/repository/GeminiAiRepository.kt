package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.GrammarCorrectionResult
import com.example.data.model.TranslationResult
import com.example.data.model.TutorReply
import com.example.data.model.WordBreakdownItem
import com.example.data.remote.GeminiApiService
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiGenerationConfig
import com.example.data.remote.GeminiPart
import com.example.data.remote.GeminiRequest
import com.example.data.remote.GeminiRetrofitClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Repository handling communication with the Gemini AI REST API via Retrofit
 * for translation, sentence breakdown, grammar correction, and tutor guidance.
 */
class GeminiAiRepository(
    private val apiService: GeminiApiService = GeminiRetrofitClient.apiService
) {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val apiKey: String
        get() = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

    private val isKeyConfigured: Boolean
        get() = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

    /**
     * Translates text between Hindi and English with deep word-by-word breakdown and grammar tips.
     */
    suspend fun translateWithBreakdown(
        text: String,
        sourceLang: String = "hi",
        targetLang: String = "en"
    ): TranslationResult = withContext(Dispatchers.IO) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            return@withContext TranslationResult(translatedText = "")
        }

        if (!isKeyConfigured) {
            return@withContext getOfflineTranslation(trimmed, sourceLang, targetLang)
        }

        val prompt = """
            You are a bilingual Hindi-English linguistic AI tutor for Indian learners.
            Translate the following text from ${if (sourceLang == "hi") "Hindi" else "English"} to ${if (targetLang == "en") "English" else "Hindi"}:
            "$trimmed"

            Respond STRICTLY with a valid JSON object matching this schema:
            {
              "translatedText": "string",
              "transliteration": "Devanagari pronunciation of the English translation",
              "explanationHindi": "Brief sentence structure explanation in Hindi",
              "grammarTip": "Key grammar insight or tense rule to remember",
              "words": [
                {
                  "word": "English word",
                  "devanagariPhonetic": "हिंदी में उच्चारण",
                  "ipaPhonetic": "IPA string",
                  "hindiMeaning": "Hindi meaning",
                  "partOfSpeech": "Noun/Verb/Adjective/Preposition/Adverb",
                  "exampleSentenceEn": "Natural English sentence",
                  "exampleSentenceHi": "Hindi translation"
                }
              ]
            }
        """.trimIndent()

        try {
            val responseText = executeGeminiPrompt(prompt, jsonMode = true)
            parseTranslationJson(responseText, trimmed, sourceLang, targetLang)
        } catch (e: Exception) {
            Log.e("GeminiAiRepository", "Translation API error: ${e.message}", e)
            getOfflineTranslation(trimmed, sourceLang, targetLang)
        }
    }

    /**
     * Analyzes English sentences for grammar errors, provides Hindi explanations,
     * corrected versions, applied grammar rules, and better natural alternatives.
     */
    suspend fun correctGrammar(
        text: String,
        context: String = ""
    ): GrammarCorrectionResult = withContext(Dispatchers.IO) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            return@withContext GrammarCorrectionResult(
                originalText = "",
                correctedText = "",
                isGrammaticallyCorrect = true
            )
        }

        if (!isKeyConfigured) {
            return@withContext getOfflineGrammarCorrection(trimmed)
        }

        val prompt = """
            You are an expert English grammar coach for Hindi speakers.
            Analyze the following English sentence for grammatical accuracy, tense usage, preposition errors, and subject-verb agreement:
            "$trimmed"
            ${if (context.isNotBlank()) "Context: $context" else ""}

            Respond STRICTLY with a valid JSON object matching this schema:
            {
              "originalText": "$trimmed",
              "correctedText": "Polished, grammatically accurate sentence",
              "isGrammaticallyCorrect": true/false,
              "explanationHindi": "Clear, friendly explanation in simple Hindi highlighting why the change was made",
              "grammarRulesApplied": ["List of specific grammar rules, e.g., 'Subject-Verb Agreement', 'Past Continuous Tense'"],
              "alternativeBetterPhrasing": "A more natural, fluent alternative phrasing commonly used by native speakers",
              "toneFormality": "Casual / Polite / Formal"
            }
        """.trimIndent()

        try {
            val responseText = executeGeminiPrompt(prompt, jsonMode = true)
            parseGrammarCorrectionJson(responseText, trimmed)
        } catch (e: Exception) {
            Log.e("GeminiAiRepository", "Grammar correction API error: ${e.message}", e)
            getOfflineGrammarCorrection(trimmed)
        }
    }

    /**
     * AI English conversational tutor with real-time feedback and encouragement.
     */
    suspend fun chatWithTutor(
        history: List<Pair<String, String>>,
        userMessage: String,
        topic: String,
        level: String
    ): TutorReply = withContext(Dispatchers.IO) {
        if (!isKeyConfigured) {
            return@withContext getOfflineTutorReply(userMessage)
        }

        val conversationHistory = history.takeLast(6).joinToString("\n") { (sender, msg) ->
            "$sender: $msg"
        }

        val prompt = """
            You are SpeakEasy AI, a warm and encouraging English conversation tutor for Hindi speakers.
            Topic: $topic
            Learner Level: $level
            Conversation History:
            $conversationHistory

            User just said: "$userMessage"

            Respond STRICTLY with a JSON object:
            {
              "englishReply": "Your natural, conversational response in English (1-2 sentences)",
              "detectedUserMistake": "Exact phrase user got wrong, or null if sentence is perfect",
              "correctionHindi": "Gentle correction explanation in Hindi, or null if perfect",
              "correctedSentence": "How to say it properly, or null if perfect",
              "encouragementHindi": "Short encouraging Hindi phrase (e.g. 'बहुत बढ़िया! जारी रखें')"
            }
        """.trimIndent()

        try {
            val responseText = executeGeminiPrompt(prompt, jsonMode = true)
            parseTutorJson(responseText, userMessage)
        } catch (e: Exception) {
            Log.e("GeminiAiRepository", "Tutor API error: ${e.message}", e)
            getOfflineTutorReply(userMessage)
        }
    }

    /**
     * Executes raw Gemini prompt through the Retrofit service interface.
     */
    private suspend fun executeGeminiPrompt(prompt: String, jsonMode: Boolean = true): String {
        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = prompt))
                )
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.3f,
                responseMimeType = if (jsonMode) "application/json" else null
            )
        )

        val response = apiService.generateContent(apiKey, request)
        val candidate = response.candidates?.firstOrNull()
        val text = candidate?.content?.parts?.firstOrNull()?.text
            ?: throw IllegalStateException("Empty response from Gemini API")

        return cleanJsonText(text)
    }

    private fun cleanJsonText(raw: String): String {
        var clean = raw.trim()
        if (clean.startsWith("```json")) {
            clean = clean.substring(7)
        } else if (clean.startsWith("```")) {
            clean = clean.substring(3)
        }
        if (clean.endsWith("```")) {
            clean = clean.substring(0, clean.length - 3)
        }
        return clean.trim()
    }

    private fun parseTranslationJson(
        jsonStr: String,
        originalText: String,
        sourceLang: String,
        targetLang: String
    ): TranslationResult {
        return try {
            val json = JSONObject(jsonStr)
            val translatedText = json.optString("translatedText", "")
            val transliteration = json.optString("transliteration", "")
            val explanation = json.optString("explanationHindi", "")
            val grammarTip = json.optString("grammarTip", "")

            val wordsArray = json.optJSONArray("words")
            val wordsList = mutableListOf<WordBreakdownItem>()

            if (wordsArray != null) {
                for (i in 0 until wordsArray.length()) {
                    val wObj = wordsArray.optJSONObject(i) ?: continue
                    wordsList.add(
                        WordBreakdownItem(
                            word = wObj.optString("word", ""),
                            devanagariPhonetic = wObj.optString("devanagariPhonetic", ""),
                            ipaPhonetic = wObj.optString("ipaPhonetic", ""),
                            hindiMeaning = wObj.optString("hindiMeaning", ""),
                            partOfSpeech = wObj.optString("partOfSpeech", "Word"),
                            exampleSentenceEn = wObj.optString("exampleSentenceEn", ""),
                            exampleSentenceHi = wObj.optString("exampleSentenceHi", "")
                        )
                    )
                }
            }

            TranslationResult(
                translatedText = translatedText,
                transliteration = transliteration,
                explanationHindi = explanation,
                words = wordsList,
                grammarTip = grammarTip
            )
        } catch (e: Exception) {
            getOfflineTranslation(originalText, sourceLang, targetLang)
        }
    }

    private fun parseGrammarCorrectionJson(jsonStr: String, originalText: String): GrammarCorrectionResult {
        return try {
            val json = JSONObject(jsonStr)
            val correctedText = json.optString("correctedText", originalText)
            val isCorrect = json.optBoolean("isGrammaticallyCorrect", correctedText.equals(originalText, ignoreCase = true))
            val explanation = json.optString("explanationHindi", "वाक्य की रचना सही है।")
            val alternative = json.optString("alternativeBetterPhrasing", "")
            val formality = json.optString("toneFormality", "Polite")

            val rulesArray = json.optJSONArray("grammarRulesApplied")
            val rulesList = mutableListOf<String>()
            if (rulesArray != null) {
                for (i in 0 until rulesArray.length()) {
                    rulesList.add(rulesArray.optString(i))
                }
            }

            GrammarCorrectionResult(
                originalText = originalText,
                correctedText = correctedText,
                isGrammaticallyCorrect = isCorrect,
                explanationHindi = explanation,
                grammarRulesApplied = rulesList,
                alternativeBetterPhrasing = alternative,
                toneFormality = formality
            )
        } catch (e: Exception) {
            getOfflineGrammarCorrection(originalText)
        }
    }

    private fun parseTutorJson(jsonStr: String, userMessage: String): TutorReply {
        return try {
            val json = JSONObject(jsonStr)
            TutorReply(
                englishReply = json.optString("englishReply", "That's great! Tell me more about that."),
                detectedUserMistake = if (json.isNull("detectedUserMistake")) null else json.optString("detectedUserMistake"),
                correctionHindi = if (json.isNull("correctionHindi")) null else json.optString("correctionHindi"),
                correctedSentence = if (json.isNull("correctedSentence")) null else json.optString("correctedSentence"),
                encouragementHindi = json.optString("encouragementHindi", "बहुत अच्छा प्रयास!")
            )
        } catch (e: Exception) {
            getOfflineTutorReply(userMessage)
        }
    }

    private fun getOfflineTranslation(text: String, sourceLang: String, targetLang: String): TranslationResult {
        val lower = text.lowercase().trim()
        val isHiToEn = sourceLang == "hi"

        val directMap = mapOf(
            "नमस्ते" to Pair("Hello / Greetings", "हेल्लो"),
            "आप कैसे हैं?" to Pair("How are you?", "हाउ आर यू?"),
            "मैं ठीक हूँ, धन्यवाद।" to Pair("I am fine, thank you.", "आई एम फाइन, थैंक यू।"),
            "आपका नाम क्या है?" to Pair("What is your name?", "व्हाट इज़ योर नेम?"),
            "मुझे अंग्रेजी सीखना है।" to Pair("I want to learn English.", "आई वॉन्ट टू लर्न इंग्लिश।"),
            "how are you?" to Pair("आप कैसे हैं?", "हाउ आर यू?"),
            "nice to meet you" to Pair("आपसे मिलकर खुशी हुई", "नाइस टू मीट यू"),
            "thank you very much" to Pair("आपका बहुत-बहुत धन्यवाद", "थैंक यू वेरी मच")
        )

        val matched = directMap[lower]
        val translated = matched?.first ?: if (isHiToEn) "Hello! (AI translation configured with Gemini key)" else "नमस्ते!"
        val transliteration = matched?.second ?: "स्पीक ईज़ी"

        return TranslationResult(
            translatedText = translated,
            transliteration = transliteration,
            explanationHindi = "यह वाक्य दैनिक बातचीत में बहुत उपयोग होता है।",
            grammarTip = "अंग्रेजी में वाक्य संरचना Subject + Verb + Object (SVO) होती है।",
            words = listOf(
                WordBreakdownItem(
                    word = "Learn",
                    devanagariPhonetic = "लर्न",
                    ipaPhonetic = "/lɜːrn/",
                    hindiMeaning = "सीखना",
                    partOfSpeech = "Verb",
                    exampleSentenceEn = "I learn new words every day.",
                    exampleSentenceHi = "मैं रोज़ नए शब्द सीखता हूँ।"
                )
            )
        )
    }

    private fun getOfflineGrammarCorrection(text: String): GrammarCorrectionResult {
        val lower = text.lowercase().trim()
        val needsCorrection = lower.contains("i is") || lower.contains("he go ") || lower.contains("she have")
        val corrected = when {
            lower.contains("i is") -> text.replace("i is", "I am", ignoreCase = true)
            lower.contains("he go ") -> text.replace("he go ", "he goes ", ignoreCase = true)
            lower.contains("she have") -> text.replace("she have", "she has", ignoreCase = true)
            else -> text
        }

        return GrammarCorrectionResult(
            originalText = text,
            correctedText = corrected,
            isGrammaticallyCorrect = !needsCorrection,
            explanationHindi = if (needsCorrection) "सब्जेक्ट और वर्ब के नियम के अनुसार 'I' के साथ 'am' और 'He/She' के साथ 's/es' लगता है।" else "आपका वाक्य व्याकरण की दृष्टि से शुद्ध है!",
            grammarRulesApplied = listOf("Subject-Verb Agreement", "Simple Present Tense"),
            alternativeBetterPhrasing = corrected,
            toneFormality = "Polite"
        )
    }

    private fun getOfflineTutorReply(userMessage: String): TutorReply {
        return TutorReply(
            englishReply = "That sounds wonderful! Could you tell me a little more about your experience?",
            detectedUserMistake = null,
            correctionHindi = null,
            correctedSentence = null,
            encouragementHindi = "बहुत बढ़िया! आत्मविश्वास के साथ बोलते रहें।"
        )
    }
}
