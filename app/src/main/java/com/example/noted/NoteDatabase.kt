package com.example.noted

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/***
 * Database that stores the notes table. It contains the Note entity.
 * It should have 4 columns, with the first one being an integer for the Note id,
 * second and third one being the Note title and body, and the last one being
 * the date the Note was set to be.
 */
@Database(entities = [Note::class], version = 3)
abstract class NoteDatabase : RoomDatabase(){

    abstract fun notesDAO(): NotesDAO

    companion object {
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java, "notes"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}