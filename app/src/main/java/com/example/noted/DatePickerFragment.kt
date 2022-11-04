package com.example.noted

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.DateFormat
import java.util.*

class DatePickerFragment(val dateButton: Button): DialogFragment(), DatePickerDialog.OnDateSetListener {

    /***
     * When the date picker dialog is created, it will be initialized with the current date by default
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    /***
     * When a date is selected by the user, the onDateSet function will take in the date and set the
     * date in the calendar instance to match it.
     * The date will then be reflected in the pick date button. The user can re-pick a date as many
     * times they like until the click the add note button.
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val selectedDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.time)
        dateButton.text = selectedDate
    }


}