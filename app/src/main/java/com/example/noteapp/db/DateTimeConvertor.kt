package com.example.noteapp.db

import androidx.room.TypeConverter
import com.example.noteapp.until.Constant.df
import java.util.*

object DateTimeConvertor {

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(date: String?): Date? {
        return if (date == null) null else df.parse(date)
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: Date?): String? {
        return df.format(date)
    }

}