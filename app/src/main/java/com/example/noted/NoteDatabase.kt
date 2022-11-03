package com.example.noted

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 2)
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