package com.example.noteapp.presenter

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.db.NoteDao
import com.example.noteapp.db.NoteDatabase
import com.example.noteapp.event.INoteModify
import com.example.noteapp.model.Note
import com.example.noteapp.until.Constant.DELETE_NOTE_SUCCESSFUL
import com.example.noteapp.until.Constant.FILE_BACKUP
import com.example.noteapp.until.Constant.SAVE_NOTE_SUCCESSFUL
import com.example.noteapp.until.Constant.UPDATE_NOTE_SUCCESSFUL
import com.example.noteapp.until.Constant.convertToDate
import com.example.noteapp.until.Constant.dc
import com.example.noteapp.until.Constant.df
import com.example.noteapp.until.Constant.getDayOfMonthFromDate
import com.example.noteapp.until.Constant.getMonthFromDate
import com.example.noteapp.until.Constant.getYearFromDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDate

class NotePresenter {
    private var context: Context? = null
    private var iNoteModify: INoteModify? = null
    private var iNoteExists: INoteModify.Check? = null
    private var iNoteList: INoteModify.NoteList? = null
    private var iNoteBackup: INoteModify.Backup? = null
    private var iItemCal: INoteModify.Item? = null
    private var dao: NoteDao? = null

    constructor(context: Context, iNoteModify: INoteModify, iNoteExists: INoteModify.Check) {
        this.context = context
        this.iNoteModify = iNoteModify
        this.iNoteExists = iNoteExists
        dao = NoteDatabase.getDatabase(context).noteDao
    }

    constructor(context: Context, iItemCal: INoteModify.Item) {
        this.context = context
        this.iItemCal = iItemCal
        dao = NoteDatabase.getDatabase(context).noteDao
    }

    constructor(
        context: Context,
        iNoteList: INoteModify.NoteList,
        iNoteBackup: INoteModify.Backup,
    ) {
        this.context = context
        this.iNoteList = iNoteList
        this.iNoteBackup = iNoteBackup
        dao = NoteDatabase.getDatabase(context).noteDao
    }

    fun save(note: Note) {
        var type: Int? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val d = getDayOfMonthFromDate(note.date)
                val m = getMonthFromDate(note.date)
                val y = getYearFromDate(note.date)
                val checkNote = dao?.getNote(d, m, y)
                if (checkNote == null) {
                    type = SAVE_NOTE_SUCCESSFUL
                    dao?.addNote(note)
                    iNoteModify?.onSuccessful(SAVE_NOTE_SUCCESSFUL)
                } else {
                    note.id = checkNote.id
                    type = UPDATE_NOTE_SUCCESSFUL
                    dao?.updateNote(note)
                    iNoteModify?.onSuccessful(UPDATE_NOTE_SUCCESSFUL)
                }

            } catch (e: Exception) {
                when (type) {
                    SAVE_NOTE_SUCCESSFUL -> iNoteModify?.onFailure("Loi add")
                    UPDATE_NOTE_SUCCESSFUL -> iNoteModify?.onFailure("Loi update")
                }
            }
        }
    }

    fun delete(d: String?, m: String?, y: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val note = dao?.getNote(d!!, m!!, y!!) ?: return@launch
                dao?.deleteNote(note)
                iNoteModify?.onSuccessful(DELETE_NOTE_SUCCESSFUL)
            } catch (e: Exception) {
//                withContext(Dispatchers.Main){
//                    iNoteModify?.onFailure("Loi delete")
//                }
            }
        }
    }

    fun checkItem(position: Int, date: LocalDate, textView: TextView) {
        val day = dc.format(textView.text.toString().toInt())
        CoroutineScope(Dispatchers.IO).launch {
            val dateInMonthList =
                dao?.getAllNoteInMonth(dc.format(date.month.value), date.year.toString())
            if (dateInMonthList != null) {
                for (item in dateInMonthList)
                    if (day == getDayOfMonthFromDate(item.date))
                        iItemCal?.onChangeBackground(position, textView)
            }
        }
    }

    fun checkNote(d: String?, m: String?, y: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val note = dao?.getNote(d!!,m!!,y!!)
            if (note != null) {
                withContext(Dispatchers.Main) {
                    iNoteExists?.onExists(note)
                }
            }
        }
    }

    fun getNoteList() {
        CoroutineScope(Dispatchers.IO).launch {
            iNoteList?.onGetNoteList(dao?.getAllNote())
        }
    }

    fun backup() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fos = context?.openFileOutput(FILE_BACKUP, AppCompatActivity.MODE_PRIVATE)
                val osw = OutputStreamWriter(fos, "UTF-8")
                val bw = BufferedWriter(osw)
                val noteList = dao?.getAllNote()
                if (noteList != null) {
                    for (item in noteList) {
                        val sb = StringBuffer()
                        sb.append("${item.id}__")
                        sb.append("${df.format(item.date)}__")
                        sb.append("${item.title}__")
                        sb.append("${item.content}")
                        bw.write(sb.toString())
                        bw.newLine()
                    }
                    bw.flush()
                    bw.close()
                }
                iNoteBackup?.onBackup("${context?.filesDir}/$FILE_BACKUP")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun convertFileToObject(sb: StringBuilder): Note {
        var note: Note?
        val values: List<String> = sb.split("__")
        note = Note(
            values[0].toInt(),
            convertToDate(values[1]),
            values[2],
            values[3])
        return note
    }

    fun restore() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dao?.deleteAll()
                val fis = context?.openFileInput(FILE_BACKUP)
                val inputStreamReader = InputStreamReader(fis)
                val bufferReader = BufferedReader(inputStreamReader)
                val sb = StringBuilder()
                var s: String?
                while ((bufferReader.readLine().also { s = it }) != null) {
                    sb.append(s)
                    dao?.addNote(convertFileToObject(sb))
                    sb.clear()
                }

                fis?.read(sb.toString().encodeToByteArray())
                fis?.close()
                withContext(Dispatchers.Main) {
                    iNoteBackup?.onRestore()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}