package com.example.noteapp.event

import android.widget.TextView
import com.example.noteapp.model.Note

interface INoteModify {
    fun onSuccessful(type: Int)
    fun onFailure(mes: String)

    interface Item {
        fun onChangeBackground(position: Int, textView: TextView)
        fun onNotChangeBackground()
    }

    interface Check {
        fun onExists(note: Note)
    }

    interface NoteList {
        suspend fun onGetNoteList(noteList: List<Note>?)
    }

    interface Backup{
        fun onBackup(path: String)
        fun onRestore()
    }
}