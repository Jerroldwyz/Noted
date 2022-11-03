package com.example.noted

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class NotesListAdapter(val context: Context, val notesList: MutableList<Note>, val noteDB: NoteDatabase): RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.layout_row, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = notesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notesList[position]
        holder.bind(item)

        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val noteToDelete = Note(notesList[position].id, notesList[position].title, notesList[position].body, notesList[position].date)
            noteDB.notesDAO().deleteNote(noteToDelete)
            (context as MainActivity).showNotes()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        holder.v.setOnLongClickListener(View.OnLongClickListener {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete note?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
                .setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
                .show()
            true
        })
    }

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