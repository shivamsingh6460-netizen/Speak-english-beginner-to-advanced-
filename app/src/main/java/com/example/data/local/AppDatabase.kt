package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BookProgressDao
import com.example.data.local.dao.ChatDao
import com.example.data.local.dao.DailyTaskDao
import com.example.data.local.dao.NoteDao
import com.example.data.local.dao.ProgressDao
import com.example.data.local.dao.SavedPhraseDao
import com.example.data.local.dao.TranslationHistoryDao
import com.example.data.local.dao.VocabularyDao
import com.example.data.local.entity.BookProgressEntity
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.DailyTaskEntity
import com.example.data.local.entity.LessonProgressEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.SavedPhraseEntity
import com.example.data.local.entity.TranslationHistoryEntity
import com.example.data.local.entity.UserProgressEntity
import com.example.data.local.entity.VocabularyEntity

@Database(
    entities = [
        VocabularyEntity::class,
        SavedPhraseEntity::class,
        TranslationHistoryEntity::class,
        ChatMessageEntity::class,
        LessonProgressEntity::class,
        UserProgressEntity::class,
        NoteEntity::class,
        DailyTaskEntity::class,
        BookProgressEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun savedPhraseDao(): SavedPhraseDao
    abstract fun translationHistoryDao(): TranslationHistoryDao
    abstract fun chatDao(): ChatDao
    abstract fun progressDao(): ProgressDao
    abstract fun noteDao(): NoteDao
    abstract fun dailyTaskDao(): DailyTaskDao
    abstract fun bookProgressDao(): BookProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "speakeasy_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(context: Context): AppDatabase = getDatabase(context)
    }
}
