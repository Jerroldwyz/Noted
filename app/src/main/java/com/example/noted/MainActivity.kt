package com.example.noted

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    //initializing the notes database using room library
    lateinit var noteDB : NoteDatabase
    lateinit var noteDAO: NotesDAO
    lateinit var recyclerNoted: RecyclerView
    lateinit var floatingBtn: FloatingActionButton
    lateinit var linearLayout: LinearLayout
    lateinit var dateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        showNotes()

        floatingBtn.setOnClickListener(){
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_note_layout)

            val addButton = dialog.findViewById<Button>(R.id.addNote)
            val noteBody = dialog.findViewById<EditText>(R.id.noteBody)
            val noteTitle = dialog.findViewById<EditText>(R.id.noteTitle)
            dateButton = dialog.findViewById<Button>(R.id.dateButton)

            addButton.setOnClickListener(){
                val title = noteTitle.text.toString()
                val body = noteBody.text.toString()

                if(body != ""){
                    var date = dateButton.text.toString()
                    if(date.equals("Pick date")){
                        val c = Calendar.getInstance()
                        date = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)
                    }
                    val noteID = noteDAO.getAllNotes().size - 1
                    noteDAO.addNote(Note(noteID, title, body, date))
                    showNotes()
                    dialog.dismiss()
                }else if(title == ""){
                    AlertDialog.Builder(this).setTitle("Invalid input").setMessage("The title of note is empty!").show()
                }else if(body ==""){
                    AlertDialog.Builder(this).setTitle("Invalid input").setMessage("The body of note is empty!").show()
                }
            }
            dialog.show()
        }
    }

    fun showNotes(){
        val notes: List<Note> = noteDAO.getAllNotes()
        if(notes.size > 0) {
            linearLayout.visibility = View.INVISIBLE
            recyclerNoted.visibility = View.VISIBLE

            recyclerNoted.adapter = NotesListAdapter(this, notes.toMutableList(), noteDB)
        }else{
            linearLayout.visibility = View.VISIBLE
            recyclerNoted.visibility = View.INVISIBLE
        }
    }

    fun initialize(){
        recyclerNoted = findViewById<RecyclerView>(R.id.recyclerNoted)
        floatingBtn = findViewById<FloatingActionButton>(R.id.fab)
        linearLayout = findViewById<LinearLayout>(R.id.Noted)

        recyclerNoted.layoutManager = GridLayoutManager(this,2)

        noteDB = NoteDatabase.getInstance(this)
        noteDAO = noteDB.notesDAO()
    }

    fun showDatePickerDialog(v: View){
        val newFragment = DatePickerFragment(dateButton)
        newFragment.show(supportFragmentManager, "datePicker")
    }
}