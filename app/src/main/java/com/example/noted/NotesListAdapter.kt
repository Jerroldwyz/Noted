package com.example.noted

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*

class NotesListAdapter(val context: Context, val notesList: MutableList<Note>, val noteDB: NoteDatabase): RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    /***
     * This function is responsible for setting the layout of the recycler view.
     * The layout that is being used is layout_row.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.layout_row, parent, false) as View
        return ViewHolder(view)
    }

    /***
     * It is an abstract method that needs to be override.
     * Returns the size of the notesList
     */
    override fun getItemCount(): Int = notesList.size

    /***
     * This function is responsible for binding each of the note item to the recycler view holder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = notesList[position]
        holder.bind(item)

        /***
         * This section of code is responsible for listening for long presses/click.
         * if the user long clicks onto an item in the recycler view (a Note item) it will prompt
         * an alert dialog that will ask if the user wants to delete the note.
         * If the user clicks Yes, the note will be deleted from the database and be updated,
         * else the dialog will just be dismissed.
         */
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val noteToDelete = Note(notesList[position].id, notesList[position].title, notesList[position].body, notesList[position].date)
            noteDB.notesDAO().deleteNote(noteToDelete)
            (context as MainActivity).showNotes()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        holder.v.setOnLongClickListener(View.OnLongClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete note?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
                .setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
                .show()
            true
        })

        /***
         * This section of code is responsible for allowing the user to edit the note
         * It reads the existing data from the Note and then updates the Note when or if the user
         * edits anything inside.
         */
        holder.v.setOnClickListener(){
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.add_note_layout)

            val addButton = dialog.findViewById<Button>(R.id.addNote)
            addButton.text = "Update"

            val noteBody = dialog.findViewById<EditText>(R.id.noteBody)
            val noteTitle = dialog.findViewById<EditText>(R.id.noteTitle)
            val dateButton = dialog.findViewById<Button>(R.id.dateButton)
            noteBody.setText(item.body)
            noteTitle.setText(item.title)
            dateButton.text = item.date

            dateButton.setOnClickListener(){
                val newFragment = DatePickerFragment(dateButton)
                newFragment.show((context as MainActivity).supportFragmentManager, "datePicker")
            }

            addButton.setOnClickListener(){
                val title = noteTitle.text.toString()
                val body = noteBody.text.toString()
                val id = item.id

                if(body != ""){
                    val date = dateButton.text.toString()
                    (context as MainActivity).noteDAO.updateNote(Note(id, title, body, date))
                    context.showNotes()
                    dialog.dismiss()
                }else if(title == ""){
                    AlertDialog.Builder(context).setTitle("Invalid input").setMessage("The title of note is empty!").show()
                }else if(body ==""){
                    AlertDialog.Builder(context).setTitle("Invalid input").setMessage("The body of note is empty!").show()
                }
            }

            dialog.show()
        }

    }

    /***
     * Sets the attributes of Note item to the view
     */
    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val title = v.findViewById<TextView>(R.id.title)
        val body = v.findViewById<TextView>(R.id.body)
        val date = v.findViewById<TextView>(R.id.date)

        fun bind(item: Note) {
            title.text = item.title
            body.text = item.body
            date.text = item.date
        }
    }
}