package com.example.noted

import android.app.Dialog
import android.content.Intent
import android.graphics.Color.blue
import android.graphics.Color.green
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    //Late init variables because they need to be used in a few functions
    //initializing the notes database using room library
    lateinit var noteDB : NoteDatabase
    //Data access object for room
    lateinit var noteDAO: NotesDAO
    //Recycler view
    lateinit var recyclerNoted: RecyclerView
    //Find view by id variables
    lateinit var floatingBtn: FloatingActionButton
    lateinit var linearLayout: LinearLayout
    lateinit var dateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        showNotes()
        onClickListener()

    }

    /***
     * Creates the menu options in the main activity with 3 choices of colors to change
     * the background color
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.color_options, menu)
        return true
    }

    /***
     * This acts as a on click listener for the items in the menu options.
     * It will change the color of the main activity background respective to the
     * option selected.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val relativeLay = findViewById<RelativeLayout>(R.id.relativeLayout)
        return when (item.itemId) {
            R.id.yellow -> {
                relativeLay.setBackgroundResource(R.color.bgYellow)
                true
            }
            R.id.green -> {
                relativeLay.setBackgroundResource(R.color.bgGreen)
                true
            }
            R.id.blue -> {
                relativeLay.setBackgroundResource(R.color.bgBlue)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /***
     * Show notes is a function that will check if there are existing notes in the app
     * If there is none, it will remain the initial layout
     * Else, it would make the recycler view visible and initialize the recycler view adapter
     ***/
    fun showNotes(){
        val notes: List<Note> = noteDAO.getAllNotes()
        if(notes.isNotEmpty()) {
            linearLayout.visibility = View.INVISIBLE
            recyclerNoted.visibility = View.VISIBLE

            recyclerNoted.adapter = NotesListAdapter(this, notes.toMutableList(), noteDB)
        }else{
            linearLayout.visibility = View.VISIBLE
            recyclerNoted.visibility = View.INVISIBLE
        }
    }

    /***
     * Initialize is a function that initializes all the necessary components of the app
     * It assigns each of the item in the main activity layout a variable
     * It sets the recycler view layout to a 2-grid layout
     * It gets the instance of the database and its data access object
     ***/
    private fun initialize(){
        recyclerNoted = findViewById<RecyclerView>(R.id.recyclerNoted)
        floatingBtn = findViewById<FloatingActionButton>(R.id.fab)
        linearLayout = findViewById<LinearLayout>(R.id.Noted)
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_note_layout)
        dateButton = dialog.findViewById<Button>(R.id.dateButton)

        recyclerNoted.layoutManager = GridLayoutManager(this,2)

        noteDB = NoteDatabase.getInstance(this)
        noteDAO = noteDB.notesDAO()
    }

    /***
     * This function listens for clicks onto buttons.
     * When the floating action button is clicked, a dialog will pop up which will prompt the user to
     * input a title, a body and a date.
     * If the user does not input a title and body, an error dialog will pop up saying the input is
     * invalid because it is empty.
     * If the date is not chosen by the user, it will by default be the current date.
     * Clicking the add button adds the note to the NoteDatabase and calls the showNotes function
     ***/
    private fun onClickListener(){
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
    /***
     * showDatePickerDialog is responsible for showing the date picker dialog
     * when the pick date button is clicked on.
     * This function is called in the add_note_layout.xml
     ***/
    fun showDatePickerDialog(v: View){
        val newFragment = DatePickerFragment(dateButton)
        newFragment.show(supportFragmentManager, "datePicker")
    }

}