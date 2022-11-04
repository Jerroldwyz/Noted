package com.example.noted

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * Class for a Note object, having a title, a body and a date.
 * It is also the entity for the note database.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "body") val body: String?,
    @ColumnInfo(name = "date") val date: String?
){

}

