package com.example.noteapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val date: Date,
    val title: String,
    val content: String,
) : Parcelable
