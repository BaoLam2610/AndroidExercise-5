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

    @Query("SELECT * FROM Note ORDER BY substr(date,7,4)||substr(date,4,2)||substr(date,1,2) DESC")
    fun getAllNote(): List<Note>

    @Query("SELECT * FROM Note WHERE substr(date,4,2)= :m AND substr(date,7,4)= :y")
    fun getAllNoteInMonth(m: String, y: String): List<Note>

    @Query("SELECT * FROM Note WHERE substr(date,1,2)= :d AND substr(date,4,2)= :m AND substr(date,7,4)= :y")
    fun getNote(d: String, m: String, y: String): Note

    @Query("DELETE FROM Note")
    suspend fun deleteAll()
}