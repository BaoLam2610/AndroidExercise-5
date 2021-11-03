package com.example.noteapp.db

import androidx.room.*
import com.example.noteapp.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note")
    fun getAllNote(): List<Note>

    @Query("SELECT * FROM Note WHERE month = :m AND year = :y")
    fun getAllNoteInMonth(m: Int, y: Int): List<Note>

    @Query("SELECT * FROM Note WHERE day = :d AND month = :m AND year = :y")
    fun getNote(d: Int, m: Int, y: Int): Note

    @Query("DELETE FROM Note")
    suspend fun deleteAll()
}