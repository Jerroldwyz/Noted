package com.example.noted

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "body") val body: String?,
    @ColumnInfo(name = "date") val date: String?
){

}

