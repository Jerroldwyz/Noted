package com.example.noted

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

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
        val item = notesList[position]
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