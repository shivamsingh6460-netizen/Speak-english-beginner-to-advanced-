package com.example.data.model

data class PronunciationExercise(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val soundCategory: String, // "V vs W", "Retroflex vs Dental (T/D/TH)", "Silent Letters", "Vowel Pairs"
    val targetPhrase: String,
    val phoneticDevanagari: String,
    val phoneticIpa: String,
    val keySoundTip: String,
    val commonHindiMistake: String,
    val exampleWords: List<String>
)

object PronunciationData {
    val exercises = listOf(
        PronunciationExercise(
            id = "pron_1",
            titleEn = "V vs W Distinction",
            titleHi = "V और W की आवाज़ का अंतर",
            soundCategory = "V vs W",
            targetPhrase = "We visited a very warm village in the west.",
            phoneticDevanagari = "वी विज़िटेड अ वेरी वॉर्म विलेज इन द वेस्ट",
            phoneticIpa = "/wiː ˈvɪz.ɪ.tɪd ə ˈveri wɔːm ˈvɪl.ɪdʒ ɪn ðə west/",
            keySoundTip = "• 'V' बोलते समय ऊपर के दाँत नीचे के होंठ को हल्के से छूते हैं (Bite lower lip).\n• 'W' बोलते समय होंठ गोल (Round lips like an 'O') बनते हैं।",
            commonHindiMistake = "हिंदी भाषी 'V' और 'W' दोनों को 'व' बोल देते हैं जिससे 'Very' और 'Wary' या 'Vine' और 'Wine' एक जैसे लगते हैं।",
            exampleWords = listOf("Very (दाँत + होंठ)", "Water (गोल होंठ)", "Village", "Window", "Voice", "World")
        ),
        PronunciationExercise(
            id = "pron_2",
            titleEn = "The Soft 'TH' Sound (Dental vs Retroflex)",
            titleHi = "'TH' की कोमल ध्वनि (थ / द नहीं)",
            soundCategory = "TH Sound",
            targetPhrase = "Thank you for thinking about these three things.",
            phoneticDevanagari = "थैंक यू फ़ॉर थिंकिंग अबाउट दीज़ थ्री थिंग्स",
            phoneticIpa = "/θæŋk juː fɔːr ˈθɪŋ.kɪŋ əˈbaʊt ðiːz θriː θɪŋz/",
            keySoundTip = "'TH' बोलने के लिए जीभ की नोक को ऊपर और नीचे के दाँतों के बीच हल्का सा बाहर निकालें और हवा छोड़ें (Tongue between teeth).",
            commonHindiMistake = "'Thank' को भारी 'टैंक' (Tank) या 'Tink' जैसा बोलना।",
            exampleWords = listOf("Thank", "Think", "Three", "Together", "Breathe", "Although")
        ),
        PronunciationExercise(
            id = "pron_3",
            titleEn = "Silent Letters in English",
            titleHi = "मूक अक्षर (Silent Letters) का सही उच्चारण",
            soundCategory = "Silent Letters",
            targetPhrase = "I have no doubt about Wednesday's schedule.",
            phoneticDevanagari = "आई हैव नो डाउट अबाउट वेन्ज़डेज़ स्केड्यूल",
            phoneticIpa = "/aɪ hæv nəʊ daʊt əˈbaʊt ˈwenz.deɪz ˈskedʒ.uːl/",
            keySoundTip = "• 'Doubt' में 'b' साइलेंट है -> डाउट\n• 'Wednesday' में 'd' साइलेंट है -> वेन्ज़डे\n• 'Receipt' में 'p' साइलेंट है -> रिसीट",
            commonHindiMistake = "लिखे हुए हर अक्षर को बोलने की कोशिश करना (जैसे 'डाउ-ब्ट' या 'वेड-नेस-डे')।",
            exampleWords = listOf("Doubt (डाउट)", "Wednesday (वेन्ज़डे)", "Receipt (रिसीट)", "Debt (डेट)", "Island (आईलैंड)", "Salmon (सैमन)")
        ),
        PronunciationExercise(
            id = "pron_4",
            titleEn = "Short vs Long Vowels (Ship vs Sheep)",
            titleHi = "ह्रस्व और दीर्घ स्वर (इ vs ई)",
            soundCategory = "Vowel Length",
            targetPhrase = "Please sit on this comfortable seat and eat a sweet.",
            phoneticDevanagari = "प्लीज़ सिट ऑन दिस कम्फ़र्टेबल सीट ऐंड ईट अ स्वीट",
            phoneticIpa = "/pliːz sɪt ɒn ðɪs ˈkʌm.fə.tə.bəl siːt ænd iːt ə swiːt/",
            keySoundTip = "• 'Sit' = छोटी 'इ' (Short crisp vowel)\n• 'Seat' = लंबी 'ई' (Long stretched vowel)",
            commonHindiMistake = "'Leave' (छोड़ना) और 'Live' (रहना) में स्वर की लंबाई का फर्क न करना।",
            exampleWords = listOf("Sit / Seat", "Ship / Sheep", "Fit / Feet", "Slip / Sleep", "Live / Leave")
        ),
        PronunciationExercise(
            id = "pron_5",
            titleEn = "Word Stress & Career vs Carrier",
            titleHi = "शब्दों पर दबाव (Stress) और आम भ्रांतियाँ",
            soundCategory = "Word Stress",
            targetPhrase = "I want to build a successful career in technology.",
            phoneticDevanagari = "आई वॉन्ट टू बिल्ड अ सक्सेसफुल करीअर इन टेक्नॉलजी",
            phoneticIpa = "/aɪ wɒnt tuː bɪld ə səkˈses.fəl kəˈrɪər ɪn tekˈnɒl.ə.dʒi/",
            keySoundTip = "• 'Career' (पेशा) = क-री-अर (Stress on second syllable: ca-REER)\n• 'Carrier' (सामान ले जाने वाला) = कै-रि-अर (Stress on first syllable)",
            commonHindiMistake = "'Career' को 'Carrier' की तरह 'कैरियर' बोलना।",
            exampleWords = listOf("Career (क-री-अर)", "Comfortable (कम्फ-टबल)", "Development (डि-वेलप-मेंट)", "Hotel (हो-टेल)")
        )
    )
}
