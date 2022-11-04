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
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "body") var body: String?,
    @ColumnInfo(name = "date") var date: String?
){

}

