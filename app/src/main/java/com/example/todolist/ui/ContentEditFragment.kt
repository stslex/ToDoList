package com.example.todolist.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todolist.R
import com.example.todolist.databinding.FragmentContentEditBinding
import com.example.todolist.util.APP_ACTIVITY
import com.example.todolist.util.hideKeyboard
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class ContentEditFragment : Fragment() {

    private lateinit var binding: FragmentContentEditBinding
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
        APP_ACTIVITY.actionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY.actionBar?.setHomeButtonEnabled(true)

        initFields()
        initListeners()

    }

    private fun initListeners() {
        binding.editContentTvDate.setOnClickListener {
            DatePickerDialog(
                APP_ACTIVITY, dateListener(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.editContentTvTime.setOnClickListener {
            TimePickerDialog(
                APP_ACTIVITY, timeListener(),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.editContentTvPriority.setOnClickListener {
            showDialogPriority()
        }

        binding.editContentTvStatus.setOnClickListener {
            showDialogStatus()
        }
    }

    private fun dateListener() =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val format = "dd.MM.yyyy"
            val sdf = SimpleDateFormat(format, Locale.US)
            binding.editContentTvDate.text = sdf.format(calendar.time)

        }

    private fun timeListener() = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val format = "kk.mm"
        val sdf = SimpleDateFormat(format, Locale.US)
        binding.editContentTvTime.text = sdf.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initFields() {
        binding.editContentTvDate.text =
            SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        binding.editContentTvTime.text =
            SimpleDateFormat("kk.mm").format(System.currentTimeMillis())
        calendar = Calendar.getInstance()
    }

    private fun showDialogPriority(){
        val dialog = Dialog(APP_ACTIVITY)
        dialog.setContentView(R.layout.dialog_priority)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dr_item_recycler)
        dialog.show()

        val buttonSave = dialog.findViewById<MaterialButton>(R.id.dialog_priority_button_save)
        buttonSave.setOnClickListener {
            dialog.hide()
        }
    }

    private fun showDialogStatus(){
        val dialog = Dialog(APP_ACTIVITY)
        dialog.setContentView(R.layout.dialog_status)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dr_item_recycler)
        dialog.show()

        val buttonSave = dialog.findViewById<MaterialButton>(R.id.dialog_status_button_save)
        buttonSave.setOnClickListener {
            dialog.hide()
        }
    }

}