package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.audio.TtsManager
import com.example.data.local.AppDatabase
import com.example.data.remote.GeminiClient
import com.example.data.repository.ChatRepository
import com.example.data.repository.DailyTaskRepository
import com.example.data.repository.LessonRepository
import com.example.data.repository.LibraryRepository
import com.example.data.repository.NoteRepository
import com.example.data.repository.ProgressRepository
import com.example.data.repository.SavedPhraseRepository
import com.example.data.repository.TranslationRepository
import com.example.data.repository.VocabularyRepository
import kotlinx.coroutines.launch
import com.example.ui.screens.conversation.ConversationScreen
import com.example.ui.screens.conversation.ConversationViewModel
import com.example.ui.screens.conversation.ConversationViewModelFactory
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.dashboard.DashboardViewModel
import com.example.ui.screens.dashboard.DashboardViewModelFactory
import com.example.ui.screens.lessons.LessonsScreen
import com.example.ui.screens.lessons.LessonsViewModel
import com.example.ui.screens.lessons.LessonsViewModelFactory
import com.example.ui.screens.library.LibraryScreen
import com.example.ui.screens.library.LibraryViewModel
import com.example.ui.screens.library.LibraryViewModelFactory
import com.example.ui.screens.notepad.NotepadScreen
import com.example.ui.screens.notepad.NotepadViewModel
import com.example.ui.screens.notepad.NotepadViewModelFactory
import com.example.ui.screens.pronunciation.PronunciationScreen
import com.example.ui.screens.pronunciation.PronunciationViewModel
import com.example.ui.screens.pronunciation.PronunciationViewModelFactory
import com.example.ui.screens.translate.TranslateScreen
import com.example.ui.screens.translate.TranslateViewModel
import com.example.ui.screens.translate.TranslateViewModelFactory
import com.example.ui.screens.vocabulary.VocabularyScreen
import com.example.ui.screens.vocabulary.VocabularyViewModel
import com.example.ui.screens.vocabulary.VocabularyViewModelFactory
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.SpeakEasyTheme

enum class Screen(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    TRANSLATE("Translate", Icons.Default.Translate),
    CHAT("AI Tutor", Icons.Default.ChatBubbleOutline),
    PRONUNCIATION("Sounds", Icons.Default.Hearing),
    LIBRARY("Library", Icons.Default.AutoStories),
    NOTEPAD("Notepad", Icons.Default.EditNote),
    LESSONS("Lessons", Icons.Default.School),
    VOCAB("Vocab", Icons.Default.Style)
}

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Init Data Layer
        val database = AppDatabase.getInstance(applicationContext)
        val geminiClient = GeminiClient()
        val vocabularyRepo = VocabularyRepository(database.vocabularyDao())
        val savedPhraseRepo = SavedPhraseRepository(database.savedPhraseDao())
        val progressRepo = ProgressRepository(database.progressDao())
        val translationRepo = TranslationRepository(database.translationHistoryDao(), geminiClient, vocabularyRepo)
        val chatRepo = ChatRepository(database.chatDao(), geminiClient, progressRepo)
        val lessonRepo = LessonRepository(database.progressDao())
        val noteRepo = NoteRepository(database.noteDao())
        val dailyTaskRepo = DailyTaskRepository(database.dailyTaskDao())
        val libraryRepo = LibraryRepository(database.bookProgressDao())

        lifecycleScope.launch {
            savedPhraseRepo.checkAndSeedInitialPhrases()
        }

        ttsManager = TtsManager(applicationContext)

        // ViewModels
        val dashboardVm: DashboardViewModel by viewModels {
            DashboardViewModelFactory(progressRepo, vocabularyRepo, lessonRepo)
        }
        val translateVm: TranslateViewModel by viewModels {
            TranslateViewModelFactory(translationRepo, vocabularyRepo)
        }
        val conversationVm: ConversationViewModel by viewModels {
            ConversationViewModelFactory(chatRepo)
        }
        val pronunciationVm: PronunciationViewModel by viewModels {
            PronunciationViewModelFactory(geminiClient, progressRepo)
        }
        val lessonsVm: LessonsViewModel by viewModels {
            LessonsViewModelFactory(lessonRepo, progressRepo)
        }
        val vocabVm: VocabularyViewModel by viewModels {
            VocabularyViewModelFactory(vocabularyRepo, progressRepo)
        }
        val libraryVm: LibraryViewModel by viewModels {
            LibraryViewModelFactory(libraryRepo, progressRepo, geminiClient)
        }
        val notepadVm: NotepadViewModel by viewModels {
            NotepadViewModelFactory(noteRepo, dailyTaskRepo, progressRepo, geminiClient)
        }

        setContent {
            SpeakEasyTheme {
                MainApp(
                    dashboardVm = dashboardVm,
                    translateVm = translateVm,
                    conversationVm = conversationVm,
                    pronunciationVm = pronunciationVm,
                    lessonsVm = lessonsVm,
                    vocabVm = vocabVm,
                    libraryVm = libraryVm,
                    notepadVm = notepadVm,
                    ttsManager = ttsManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

@Composable
fun MainApp(
    dashboardVm: DashboardViewModel,
    translateVm: TranslateViewModel,
    conversationVm: ConversationViewModel,
    pronunciationVm: PronunciationViewModel,
    lessonsVm: LessonsViewModel,
    vocabVm: VocabularyViewModel,
    libraryVm: LibraryViewModel,
    notepadVm: NotepadViewModel,
    ttsManager: TtsManager
) {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                Screen.values().forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 9.sp
                                ),
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandPrimary,
                            selectedTextColor = BrandPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition",
            modifier = Modifier.padding(innerPadding)
        ) { targetScreen ->
            when (targetScreen) {
                Screen.HOME -> DashboardScreen(
                    viewModel = dashboardVm,
                    onNavigateToTranslate = { currentScreen = Screen.TRANSLATE },
                    onNavigateToChat = { currentScreen = Screen.CHAT },
                    onNavigateToPronunciation = { currentScreen = Screen.PRONUNCIATION },
                    onNavigateToLessons = { currentScreen = Screen.LESSONS },
                    onNavigateToVocab = { currentScreen = Screen.VOCAB },
                    onNavigateToLibrary = { currentScreen = Screen.LIBRARY },
                    onNavigateToNotepad = { currentScreen = Screen.NOTEPAD },
                    onSpeakWord = { ttsManager.speak(it) }
                )
                Screen.TRANSLATE -> TranslateScreen(
                    viewModel = translateVm,
                    onSpeakText = { ttsManager.speak(it) }
                )
                Screen.CHAT -> ConversationScreen(
                    viewModel = conversationVm,
                    onSpeakText = { text, speed ->
                        ttsManager.setSpeed(speed)
                        ttsManager.speak(text)
                    }
                )
                Screen.PRONUNCIATION -> PronunciationScreen(
                    viewModel = pronunciationVm,
                    onSpeakText = { text, isSlow -> ttsManager.speak(text, isSlow) }
                )
                Screen.LIBRARY -> LibraryScreen(
                    viewModel = libraryVm,
                    onSpeakText = { ttsManager.speak(it) }
                )
                Screen.NOTEPAD -> NotepadScreen(
                    viewModel = notepadVm
                )
                Screen.LESSONS -> LessonsScreen(
                    viewModel = lessonsVm,
                    onSpeakText = { ttsManager.speak(it) }
                )
                Screen.VOCAB -> VocabularyScreen(
                    viewModel = vocabVm,
                    onSpeakText = { ttsManager.speak(it) }
                )
            }
        }
    }
}
