package com.example.data.model

data class TheorySection(
    val headingEn: String,
    val headingHi: String,
    val explanationHi: String,
    val ruleSummary: String,
    val correctExamples: List<String>,
    val incorrectExamples: List<String> = emptyList()
)

data class Pitfall(
    val wrong: String,
    val correct: String,
    val reasonHindi: String
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationHindi: String,
    val questionHi: String = ""
) {
    val questionEn: String get() = question
}

data class SpeakingChallenge(
    val promptEn: String,
    val promptHi: String,
    val targetSentence: String,
    val hintHindi: String
)

data class LessonVocabWord(
    val word: String,
    val devanagariPhonetic: String,
    val hindiMeaning: String,
    val exampleSentenceEn: String = ""
)

data class Lesson(
    val id: String,
    val dayNumber: Int,
    val titleEn: String,
    val titleHi: String,
    val category: String, // Grammar, Speaking, Vocabulary, Workplace
    val level: String, // Beginner, Intermediate, Advanced
    val durationMinutes: Int,
    val summaryHindi: String,
    val theorySections: List<TheorySection> = emptyList(),
    val commonPitfalls: List<Pitfall> = emptyList(),
    val quizQuestions: List<QuizQuestion> = emptyList(),
    val speakingChallenge: SpeakingChallenge? = null,
    val vocabulary: List<LessonVocabWord> = emptyList()
) {
    val estimatedMinutes: Int get() = durationMinutes
    val description: String get() = summaryHindi
    val quiz: List<QuizQuestion> get() = quizQuestions
    val theoryHindi: String get() = theorySections.joinToString("\n\n") { sec ->
        val examples = if (sec.correctExamples.isNotEmpty()) {
            "\n\nउचित उदाहरण:\n" + sec.correctExamples.joinToString("\n") { "• $it" }
        } else ""
        "${sec.headingEn} (${sec.headingHi})\n${sec.explanationHi}\n💡 नियम: ${sec.ruleSummary}$examples"
    }
    val commonHindiPitfall: String get() = commonPitfalls.joinToString("\n\n") {
        "❌ गलत: ${it.wrong}\n✅ सही: ${it.correct}\n💡 कारण: ${it.reasonHindi}"
    }
}

object LessonsData {
    val allLessons = listOf(
        Lesson(
            id = "lesson_1",
            dayNumber = 1,
            titleEn = "Self Introduction & Greetings",
            titleHi = "आत्म-परिचय और अभिवादन",
            category = "Speaking",
            level = "Beginner",
            durationMinutes = 15,
            summaryHindi = "सीखें कि किसी से पहली बार मिलते ही आत्मविश्वास के साथ अपना परिचय (Self Introduction) कैसे दें और आम गलतियों से कैसे बचें।",
            theorySections = listOf(
                TheorySection(
                    headingEn = "Greeting Naturally",
                    headingHi = "शुरुआत कैसे करें",
                    explanationHi = "अंग्रेजी में बातचीत शुरू करते समय 'Myself Rahul' बोलने से बचें। फॉर्मल जगहों पर 'Hello, I am Rahul' या 'My name is Rahul' का प्रयोग करें।",
                    ruleSummary = "Use 'I am...' or 'My name is...' never 'Myself...'",
                    correctExamples = listOf("Hello, my name is Amit.", "I am a software engineer from Delhi."),
                    incorrectExamples = listOf("Myself Amit from Delhi. (❌ Grammatically incorrect)")
                ),
                TheorySection(
                    headingEn = "Describing What You Do",
                    headingHi = "अपने काम या पढ़ाई के बारे में बताना",
                    explanationHi = "जब आप अपने वर्तमान काम की बात करें तो 'I work as a...' या 'I am working with...' बोलें।",
                    ruleSummary = "Subject + Verb + Role/Location",
                    correctExamples = listOf("I work as a marketing executive at XYZ company.", "I am currently pursuing my graduation.")
                )
            ),
            commonPitfalls = listOf(
                Pitfall(
                    wrong = "Myself Ramesh.",
                    correct = "My name is Ramesh. / I am Ramesh.",
                    reasonHindi = "'Myself' एक reflexive pronoun है। इसे वाक्य का subject नहीं बनाया जा सकता।"
                ),
                Pitfall(
                    wrong = "I am having two years experience.",
                    correct = "I have two years of experience.",
                    reasonHindi = "कब्जे या योग्यता दर्शाने के लिए 'have' का प्रयोग करें, 'having' का नहीं।"
                )
            ),
            vocabulary = listOf(
                LessonVocabWord("Introduce", "इंट्रोड्यूस", "परिचय देना", "Allow me to introduce myself."),
                LessonVocabWord("Pursue", "पर्स्यू", "आगे बढ़ाना / पढ़ाई जारी रखना", "I am pursuing my master's degree."),
                LessonVocabWord("Background", "बैकग्राउंड", "पृष्ठभूमि / अनुभव", "My educational background is in engineering.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which is the correct way to introduce yourself in an interview?",
                    options = listOf("Myself Priya Sharma.", "I am Priya Sharma.", "Priya is my self.", "Me Priya."),
                    correctIndex = 1,
                    explanationHindi = "'I am' या 'My name is' सबसे शुद्ध और फॉर्मल तरीका है।"
                ),
                QuizQuestion(
                    question = "Choose the correct sentence regarding work experience:",
                    options = listOf(
                        "I am having 3 years experience.",
                        "I have 3 years of experience.",
                        "I has 3 years experience.",
                        "Me have 3 years experience."
                    ),
                    correctIndex = 1,
                    explanationHindi = "'I' के साथ 'have' + 'of experience' का प्रयोग होता है।"
                )
            ),
            speakingChallenge = SpeakingChallenge(
                promptEn = "Introduce your name and hometown clearly.",
                promptHi = "अपना नाम और गृहनगर बताते हुए यह वाक्य बोलें:",
                targetSentence = "Hello, my name is Amit and I am from Jaipur.",
                hintHindi = "आवाज़ में स्पष्टता रखें और 'Hello' के बाद हल्का पॉज़ लें।"
            )
        ),
        Lesson(
            id = "lesson_2",
            dayNumber = 2,
            titleEn = "Articles (A, An, The) Demystified",
            titleHi = "A, An, The का सही और आसान उपयोग",
            category = "Grammar",
            level = "Beginner",
            durationMinutes = 15,
            summaryHindi = "स्वर (Vowel Sound) और व्यंजन (Consonant Sound) के आधार पर A और An का सही चुनाव करना सीखें।",
            theorySections = listOf(
                TheorySection(
                    headingEn = "Sound Rule for 'A' vs 'An'",
                    headingHi = "ध्वनि का नियम (Sound Rule)",
                    explanationHi = "अंग्रेजी अक्षरों पर नहीं, बल्कि हिंदी स्वर ध्वनियों (अ, आ, इ, ई, उ, ऊ, ए, ऐ, ओ, औ) पर ध्यान दें। यदि शब्द की पहली ध्वनि स्वर है तो 'An' लगेगा (जैसे: An Hour, An Honest man, An MBA).",
                    ruleSummary = "Vowel Sound -> 'An' | Consonant Sound -> 'A'",
                    correctExamples = listOf("She is an honest manager.", "He is a European traveler.", "It takes an hour.")
                ),
                TheorySection(
                    headingEn = "When to use 'The'",
                    headingHi = "'The' का खास इस्तेमाल",
                    explanationHi = "जब हम किसी निश्चित या पहले से ज्ञात वस्तु/व्यक्ति की बात करते हैं, तो 'The' लगाते हैं।",
                    ruleSummary = "Specific / Unique things -> 'The'",
                    correctExamples = listOf("I bought a book. The book is very interesting.", "The Sun rises in the East.")
                )
            ),
            commonPitfalls = listOf(
                Pitfall(
                    wrong = "He is honest person.",
                    correct = "He is an honest person.",
                    reasonHindi = "गिनने योग्य एकवचन संज्ञा (Countable Singular Noun) के आगे 'a/an' लगाना अनिवार्य है।"
                ),
                Pitfall(
                    wrong = "I study in an university.",
                    correct = "I study in a university.",
                    reasonHindi = "'University' की पहली ध्वनि 'य' (consonant 'Y' sound) है, इसलिए 'A' आएगा।"
                )
            ),
            vocabulary = listOf(
                LessonVocabWord("Honest", "ऑनेस्ट", "ईमानदार", "He is an honest police officer."),
                LessonVocabWord("Unique", "यूनिक", "अनोखा / अद्वितीय", "This is a unique opportunity."),
                LessonVocabWord("University", "यूनिवर्सिटी", "विश्वविद्यालय", "She studies at a reputable university.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Fill in the blank: 'She will arrive in ___ hour.'",
                    options = listOf("a", "an", "the", "no article"),
                    correctIndex = 1,
                    explanationHindi = "'Hour' का उच्चारण 'आवर' (स्वर) से शुरू होता है, इसलिए 'an' लगेगा।"
                ),
                QuizQuestion(
                    question = "Which article is correct for 'MBA degree'?",
                    options = listOf("A MBA degree", "An MBA degree", "The MBA degree", "None"),
                    correctIndex = 1,
                    explanationHindi = "'MBA' का उच्चारण 'एम' (ए-स्वर) से होता है, इसलिए 'an' सही है।"
                )
            ),
            speakingChallenge = SpeakingChallenge(
                promptEn = "Say this sentence emphasizing 'an honest' correctly:",
                promptHi = "इस वाक्य को बोलकर अभ्यास करें:",
                targetSentence = "He is an honest and hardworking employee.",
                hintHindi = "'honest' में 'h' साइलेंट होता है, 'ऑनेस्ट' बोलें।"
            )
        ),
        Lesson(
            id = "lesson_3",
            dayNumber = 3,
            titleEn = "Past Tense: Stop Saying 'I Didn't Knew'",
            titleHi = "भूतकाल (Past Tense) की आम गलतियाँ सुधारें",
            category = "Grammar",
            level = "Intermediate",
            durationMinutes = 20,
            summaryHindi = "हिंदी भाषियों की सबसे बड़ी गलती: 'Did' के साथ 2nd form लगा देना। जानिए Did + V1 का अचूक नियम।",
            theorySections = listOf(
                TheorySection(
                    headingEn = "The 'Did + V1' Golden Rule",
                    headingHi = "'Did' के साथ हमेशा क्रिया का पहला रूप (V1)",
                    explanationHi = "जब वाक्य में 'did' या 'didn't' आ जाता है, तो भूतकाल पहले ही दर्शा दिया जाता है। इसलिए मुख्य क्रिया हमेशा Base Form (V1) में रहेगी।",
                    ruleSummary = "Subject + didn't + V1 (Base Verb)",
                    correctExamples = listOf("I didn't know about this.", "She didn't call me yesterday.", "Did you go there?"),
                    incorrectExamples = listOf("I didn't knew. (❌)", "She didn't called. (❌)", "Did you went? (❌)")
                )
            ),
            commonPitfalls = listOf(
                Pitfall(
                    wrong = "I didn't saw him at the station.",
                    correct = "I didn't see him at the station.",
                    reasonHindi = "'didn't' के बाद 'see' (V1) आएगा, 'saw' (V2) नहीं।"
                ),
                Pitfall(
                    wrong = "Yesterday I am going to market.",
                    correct = "Yesterday I went to the market.",
                    reasonHindi = "बीते हुए समय (Yesterday) के लिए Simple Past (went) का प्रयोग करें।"
                )
            ),
            vocabulary = listOf(
                LessonVocabWord("Receive", "रिसीव", "प्राप्त करना", "I did not receive the documents."),
                LessonVocabWord("Confirm", "कन्फर्म", "पुष्टि करना", "Please confirm your availability."),
                LessonVocabWord("Yesterday", "यस्टरडे", "कल (बीता हुआ)", "I met him yesterday.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Choose the correct sentence:",
                    options = listOf(
                        "I didn't received the email.",
                        "I didn't receive the email.",
                        "I did not receiving the email.",
                        "I not received the email."
                    ),
                    correctIndex = 1,
                    explanationHindi = "'didn't' के बाद क्रिया का पहला रूप 'receive' आएगा।"
                ),
                QuizQuestion(
                    question = "What is the correct negative past form of 'They came home'?",
                    options = listOf(
                        "They didn't came home.",
                        "They didn't come home.",
                        "They did not coming home.",
                        "They not came home."
                    ),
                    correctIndex = 1,
                    explanationHindi = "'came' का base form 'come' है, इसलिए 'didn't come' होगा।"
                )
            ),
            speakingChallenge = SpeakingChallenge(
                promptEn = "Practice saying this past tense sentence smoothly:",
                promptHi = "इस वाक्य को धाराप्रवाह बोलें:",
                targetSentence = "I did not receive any notification yesterday.",
                hintHindi = "'did not' को 'didn't' या साफ़ उच्चारण के साथ बोलें।"
            )
        ),
        Lesson(
            id = "lesson_4",
            dayNumber = 4,
            titleEn = "Prepositions: In, On, At Made Crystal Clear",
            titleHi = "In, On, At का सटीक इस्तेमाल",
            category = "Grammar",
            level = "Intermediate",
            durationMinutes = 15,
            summaryHindi = "हिंदी में हम 'में' और 'पर' बोलते हैं, पर अंग्रेजी में समय और स्थान के लिए In, On, At के अलग नियम हैं।",
            theorySections = listOf(
                TheorySection(
                    headingEn = "Time Hierarchy",
                    headingHi = "समय के लिए (Time Triangle)",
                    explanationHi = "• At = सटीक समय (At 5 PM, At night)\n• On = दिन और तारीख (On Monday, On 15th August)\n• In = महीने, साल, मौसम (In July, In 2026, In summer)",
                    ruleSummary = "Specific time -> At | Days/Dates -> On | Longer periods -> In",
                    correctExamples = listOf("Let's meet at 4:30 PM.", "The meeting is on Friday.", "I was born in 1998.")
                ),
                TheorySection(
                    headingEn = "Public Transport: In vs On",
                    headingHi = "गाड़ियों में In और On का फर्क",
                    explanationHi = "जिस वाहन में आप खड़े होकर चल सकते हैं (Bus, Train, Plane) उसमें 'On' आता है। जिसमें झुककर बैठना पड़े (Car, Taxi) उसमें 'In' आता है।",
                    ruleSummary = "Walkable vehicles -> 'On the bus/train' | Small vehicles -> 'In the car/taxi'",
                    correctExamples = listOf("I am on the train.", "She is in a taxi.")
                )
            ),
            commonPitfalls = listOf(
                Pitfall(
                    wrong = "I am in the bus.",
                    correct = "I am on the bus.",
                    reasonHindi = "बस, ट्रेन, हवाई जहाज के लिए 'On' का प्रयोग स्वाभाविक और मानक है।"
                ),
                Pitfall(
                    wrong = "Meet me in 5 o'clock.",
                    correct = "Meet me at 5 o'clock.",
                    reasonHindi = "घड़ी के सटीक समय के साथ हमेशा 'At' आता है।"
                )
            ),
            vocabulary = listOf(
                LessonVocabWord("Punctual", "पंक्चुअल", "समयनिष्ठ", "He is always punctual for meetings."),
                LessonVocabWord("Commute", "कम्यूट", "आना-जाना / यात्रा करना", "I commute by train every morning."),
                LessonVocabWord("Schedule", "शेड्यूल", "समय सारणी", "The meeting is scheduled at 3 PM.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Fill in: 'The project will start ___ Monday.'",
                    options = listOf("in", "on", "at", "by"),
                    correctIndex = 1,
                    explanationHindi = "सप्ताह के दिनों (Days) के साथ 'on' का प्रयोग होता है।"
                ),
                QuizQuestion(
                    question = "Choose the correct phrase: 'I am travelling ___ the train right now.'",
                    options = listOf("on", "in", "at", "inside of"),
                    correctIndex = 0,
                    explanationHindi = "ट्रेन के लिए 'on the train' सही मानक प्रयोग है।"
                )
            ),
            speakingChallenge = SpeakingChallenge(
                promptEn = "Speak this preposition-rich sentence aloud:",
                promptHi = "इस वाक्य को स्पष्ट आवाज़ में बोलें:",
                targetSentence = "Our team meeting will start on Tuesday at ten in the morning.",
                hintHindi = "'on Tuesday' और 'at ten' पर प्राकृतिक लय रखें।"
            )
        ),
        Lesson(
            id = "lesson_5",
            dayNumber = 5,
            titleEn = "Polite Workplace English & Requests",
            titleHi = "ऑफिस और ईमेल में विनम्र अंग्रेजी बोलना",
            category = "Workplace",
            level = "Advanced",
            durationMinutes = 20,
            summaryHindi = "सीखें कि 'Give me this' की जगह 'Could you please...' जैसे विनम्र वाक्य बोलकर प्रोफेशनल माहौल में प्रभाव कैसे जमाएँ।",
            theorySections = listOf(
                TheorySection(
                    headingEn = "Magic Modal Verbs (Could / Would)",
                    headingHi = "विनम्र निवेदन के शब्द",
                    explanationHi = "सीधे 'Do this' या 'Send me file' बोलना रूखा (rude) लग सकता है। इसके स्थान पर 'Could you please send...' या 'Would you mind sharing...' का प्रयोग करें।",
                    ruleSummary = "Could you please + V1 | Would you mind + V-ing",
                    correctExamples = listOf(
                        "Could you please share the report with me?",
                        "Would you mind reviewing this document?",
                        "I would appreciate it if you could update me."
                    )
                )
            ),
            commonPitfalls = listOf(
                Pitfall(
                    wrong = "Give me your laptop for 5 minutes.",
                    correct = "Could I please borrow your laptop for 5 minutes?",
                    reasonHindi = "ऑफिस में सीधे 'Give me' के बजाय 'Could I borrow / please provide' बोलना विनम्र माना जाता है।"
                )
            ),
            vocabulary = listOf(
                LessonVocabWord("Appreciate", "अप्रीशियेट", "सराहना करना / आभारी होना", "I would appreciate your feedback."),
                LessonVocabWord("Convenient", "कन्वीनिएंट", "सुविधाजनक", "Let us meet when it is convenient."),
                LessonVocabWord("Collaborate", "कोलेबोरेट", "सहयोग करना", "We are excited to collaborate with you.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    question = "Which request is most professional?",
                    options = listOf(
                        "Send me the presentation right now.",
                        "Could you please share the presentation when convenient?",
                        "I want presentation quickly.",
                        "Give presentation."
                    ),
                    correctIndex = 1,
                    explanationHindi = "'Could you please...' सबसे आदरपूर्ण और व्यावसायिक तरीका है।"
                )
            ),
            speakingChallenge = SpeakingChallenge(
                promptEn = "Practice asking for help politely:",
                promptHi = "ऑफिस की विनम्र बातचीत का यह वाक्य बोलें:",
                targetSentence = "Could you please help me with this task when you have a moment?",
                hintHindi = "'Could you please' को कोमलता और विश्वास के साथ बोलें।"
            )
        )
    )
}
