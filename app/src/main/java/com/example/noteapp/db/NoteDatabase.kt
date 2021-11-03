package com.example.noteapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteapp.model.Note
import com.example.noteapp.until.Constant.DB_NAME

@Database(
    entities = [Note::class],
    version = 3
)
@TypeConverters(DateTimeConvertor::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {

        @Volatile
        private var instance: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase = instance ?: synchronized(this) {
            return Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration()
                .build().also {
                    instance = it
                }
        }

    }
}