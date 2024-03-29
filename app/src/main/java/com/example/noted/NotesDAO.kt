package com.example.noted

import androidx.room.*

/***
 * Data access object (DAO) of the Note object.
 * It is responsible for accessing the database by either
 * adding, deleting or getting notes from it.
 */
@Dao
interface NotesDAO {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Note>

    @Insert
    fun addNote(vararg note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)
}