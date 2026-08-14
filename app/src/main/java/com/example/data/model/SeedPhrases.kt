package com.example.data.model

import com.example.data.local.entity.SavedPhraseEntity

object SeedPhrases {
    val initialPhrases = listOf(
        SavedPhraseEntity(
            englishPhrase = "Could you please speak a little slowly?",
            hindiTranslation = "क्या आप कृपया थोड़ा धीरे बोल सकते हैं?",
            phoneticDevanagari = "कुड यू प्लीज स्पीक अ लिटिल स्लोली?",
            category = "Daily Essentials",
            situationContext = "बातचीत के दौरान जब सामने वाला बहुत तेज़ अंग्रेजी बोल रहा हो",
            formalityLevel = "Polite"
        ),
        SavedPhraseEntity(
            englishPhrase = "I did not catch that. Could you repeat please?",
            hindiTranslation = "मैं समझ नहीं पाया। क्या आप दोहरा सकते हैं?",
            phoneticDevanagari = "आई डिड नॉट कैच दैट। कुड यू रिपीट प्लीज?",
            category = "Daily Essentials",
            situationContext = "जब आपको कोई बात साफ़ सुनाई न दे या समझ न आए",
            formalityLevel = "Polite"
        ),
        SavedPhraseEntity(
            englishPhrase = "Could you please send me the updated meeting link?",
            hindiTranslation = "क्या आप कृपया मुझे मीटिंग का नया लिंक भेज सकते हैं?",
            phoneticDevanagari = "कुड यू प्लीज सेंड मी द अपडेटेड मीटिंग लिंक?",
            category = "Workplace & Office",
            situationContext = "ऑफिस या क्लाइंट से मीटिंग लिंक मांगने के लिए",
            formalityLevel = "Formal"
        ),
        SavedPhraseEntity(
            englishPhrase = "I will look into this and get back to you shortly.",
            hindiTranslation = "मैं इस मामले को देखकर जल्द ही आपको जवाब दूँगा।",
            phoneticDevanagari = "आई विल लुक इन्टू दिस एंड गेट बैक टू यू शॉर्टली।",
            category = "Workplace & Office",
            situationContext = "ईमेल या टीम चैट पर किसी कार्य की ज़िम्मेदारी लेते हुए",
            formalityLevel = "Formal"
        ),
        SavedPhraseEntity(
            englishPhrase = "How much does this cost, and is there any discount?",
            hindiTranslation = "इसकी क्या कीमत है, और क्या कोई छूट उपलब्ध है?",
            phoneticDevanagari = "हाउ मच डज़ दिस कॉस्ट, एंड इज़ देयर एनी डिस्काउंट?",
            category = "Shopping & Market",
            situationContext = "दुकान या बाज़ार में खरीदारी करते समय",
            formalityLevel = "Casual"
        ),
        SavedPhraseEntity(
            englishPhrase = "Which platform does the train to Delhi depart from?",
            hindiTranslation = "दिल्ली जाने वाली ट्रेन किस प्लेटफार्म से छूटेगी?",
            phoneticDevanagari = "विच प्लेटफॉर्म डज़ द ट्रेन टू दिल्ली डिपार्ट फ्रॉम?",
            category = "Travel & Transport",
            situationContext = "रेलवे स्टेशन पर पूछताछ करते समय",
            formalityLevel = "Polite"
        ),
        SavedPhraseEntity(
            englishPhrase = "It was a pleasure meeting you today.",
            hindiTranslation = "आज आपसे मिलकर बहुत खुशी हुई।",
            phoneticDevanagari = "इट वॉज़ अ प्लेज़र मीटिंग यू टुडे।",
            category = "Social & Greetings",
            situationContext = "मुलाकात खत्म होने के बाद विदा लेते हुए",
            formalityLevel = "Polite"
        )
    )
}
