package com.example.noteapp.until

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Constant {

    const val KEY_PASSWORD = "password"
    const val PASSWORD = "pass"
    const val TYPE_FRAGMENT = "type_fragment"
    const val TYPE_ACTIVITY = "type_fragment"
    const val DAY = "day"
    const val MONTH = "month"
    const val YEAR = "year"
    const val DB_NAME = "note_db"

    const val SAVE_NOTE_SUCCESSFUL = 0
    const val SAVE_NOTE_FAILURE = 1

    const val UPDATE_NOTE_SUCCESSFUL = 2
    const val UPDATE_NOTE_FAILURE = 3

    const val DELETE_NOTE_SUCCESSFUL = 4
    const val DELETE_NOTE_FAILURE = 5

    const val FILE_BACKUP = "note_backup.csv"

    private const val TIME_STAMP_FORMAT = "dd/MM/yyyy"

    var dtf = DateTimeFormatter.ofPattern(TIME_STAMP_FORMAT)
    var df = SimpleDateFormat(TIME_STAMP_FORMAT)
    val dc = DecimalFormat("00")

    fun convertToDate(date: String): Date {
        return df.parse(date)
    }

    fun convertToString(date: Date?): String? {
        return df.format(date)
    }

//    fun convertToDate(date: String): OffsetDateTime {
//        return ZonedDateTime.parse(date, dtf).toOffsetDateTime()
//    }

    fun getDayOfMonthFromDate(date: Date): String{
        val cal = Calendar.getInstance()
        cal.time = date
        return dc.format(cal.get(Calendar.DAY_OF_MONTH))
    }

    fun getMonthFromDate(date: Date): String{
        val cal = Calendar.getInstance()
        cal.time = date
        return dc.format(cal.get(Calendar.MONTH) + 1)
    }

    fun getYearFromDate(date: Date): String{
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.YEAR).toString()
    }

}