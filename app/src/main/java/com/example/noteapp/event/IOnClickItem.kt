package com.example.noteapp.event

import com.example.noteapp.model.Note

interface IOnClickItem {
    interface ICalendar {
        fun onDoubleClickItem(day: Int)
    }
    interface INote{
        fun onClickItem(note: Note)
    }
}