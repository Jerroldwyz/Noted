package com.example.noted

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDAO {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Note>

    @Insert
    fun addNote(vararg note: Note)

    @Delete
    fun deleteNote(note: Note)
}