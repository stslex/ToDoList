package com.example.todolist.ui.editScreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.TodoViewModel
import com.example.todolist.TodoViewModelFactory
import com.example.todolist.bd.Todo
import com.example.todolist.databinding.FragmentContentEditBinding
import com.example.todolist.util.APP_ACTIVITY
import com.example.todolist.util.hideKeyboard
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class ContentEditFragment : Fragment() {

    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((APP_ACTIVITY.application as TodoApplication).repository)
    }

    private lateinit var binding: FragmentContentEditBinding
    private lateinit var calendar: Calendar
    private val bundle = Bundle()
    private var newTodo = true
    private var id: Int? = null

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
        newTodo = arguments?.getBoolean("NewTodo") ?: true
        Log.i("newTodo::", newTodo.toString())
        if (newTodo) {
            binding.editContentTvDate.text =
                SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
            binding.editContentTvTime.text =
                SimpleDateFormat("kk.mm").format(System.currentTimeMillis())

        } else {
            id = arguments?.getInt("TodoId")
            if (id != null) {
                val todo = todoViewModel.getToDoItem(id!!)
                Log.i("id::", id.toString())
                todo.observe(APP_ACTIVITY, {
                    binding.contentTitleText.setText(it.title)
                    binding.contentContentText.setText(it.content)
                    binding.editContentTvStatus.text = it.status
                    binding.editContentTvPriority.text = it.priority
                    binding.editContentTvDate.text = it.date
                    binding.editContentTvTime.text = it.time
                })
            }
        }

        calendar = Calendar.getInstance()
    }

    private fun showDialogPriority() {
        val dialog = Dialog(APP_ACTIVITY)
        dialog.setContentView(R.layout.dialog_priority)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dr_item_recycler)
        val buttonSave = dialog.findViewById<MaterialButton>(R.id.dialog_priority_button_save)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.dialog_priority_radio_group)
        dialog.show()

        if (id != null) {
            todoViewModel.getToDoItem(id!!).observe(APP_ACTIVITY, { todo ->
                var priority = todo.priority
                setDialogPriorityRadio(priority, dialog)
                buttonSave.setOnClickListener {
                    when (radioGroup.checkedRadioButtonId) {
                        R.id.dialog_priority_radio_button_law -> priority =
                            APP_ACTIVITY.getString(R.string.priority_law)
                        R.id.dialog_priority_Radio_button_normal -> priority =
                            APP_ACTIVITY.getString(R.string.priority_normal)
                        R.id.dialog_priority_radio_button_high -> priority =
                            APP_ACTIVITY.getString(R.string.priority_high)
                    }
                    todo.priority = priority
                    todoViewModel.update(todo)
                    dialog.hide()
                }
            })
        } else {
            var priority = APP_ACTIVITY.getString(R.string.priority_normal)
            setDialogPriorityRadio(priority, dialog)
            buttonSave.setOnClickListener {
                when (radioGroup.checkedRadioButtonId) {
                    R.id.dialog_priority_radio_button_law -> priority =
                        APP_ACTIVITY.getString(R.string.priority_law)
                    R.id.dialog_priority_Radio_button_normal -> priority =
                        APP_ACTIVITY.getString(R.string.priority_normal)
                    R.id.dialog_priority_radio_button_high -> priority =
                        APP_ACTIVITY.getString(R.string.priority_high)
                }
                binding.editContentTvPriority.text = priority
                dialog.hide()
            }
        }

    }

    private fun setDialogPriorityRadio(priority: String, dialog: Dialog) {
        when (priority) {
            APP_ACTIVITY.getString(R.string.priority_law) -> {
                dialog.findViewById<RadioButton>(R.id.dialog_priority_radio_button_law).isChecked =
                    true
            }
            APP_ACTIVITY.getString(R.string.priority_normal) -> {
                dialog.findViewById<RadioButton>(R.id.dialog_priority_Radio_button_normal).isChecked =
                    true
            }
            APP_ACTIVITY.getString(R.string.priority_high) -> {
                dialog.findViewById<RadioButton>(R.id.dialog_priority_radio_button_high).isChecked =
                    true
            }
        }
    }


    private fun showDialogStatus() {
        val dialog = Dialog(APP_ACTIVITY)
        dialog.setContentView(R.layout.dialog_status)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dr_item_recycler)
        val buttonSave = dialog.findViewById<MaterialButton>(R.id.dialog_status_button_save)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.dialog_status_radio_group)
        dialog.show()

        if (id != null) {
            todoViewModel.getToDoItem(id!!).observe(APP_ACTIVITY, { todo ->
                var status = todo.status
                setDialogStatusRadio(status, dialog)
                buttonSave.setOnClickListener {
                    when (radioGroup.checkedRadioButtonId) {
                        R.id.dialog_status_radio_button_done -> status =
                            APP_ACTIVITY.getString(R.string.status_done)
                        R.id.dialog_status_radio_button_in_progress -> status =
                            APP_ACTIVITY.getString(R.string.status_in_progress)
                        R.id.dialog_status_radio_button_late -> status =
                            APP_ACTIVITY.getString(R.string.status_late)
                    }
                    todo.status = status
                    todoViewModel.update(todo)
                    dialog.hide()
                }
            })
        } else {
            var status = APP_ACTIVITY.getString(R.string.status_in_progress)
            setDialogPriorityRadio(status, dialog)
            buttonSave.setOnClickListener {
                when (radioGroup.checkedRadioButtonId) {
                    R.id.dialog_status_radio_button_done -> status =
                        APP_ACTIVITY.getString(R.string.status_done)
                    R.id.dialog_status_radio_button_in_progress -> status =
                        APP_ACTIVITY.getString(R.string.status_in_progress)
                    R.id.dialog_status_radio_button_late -> status =
                        APP_ACTIVITY.getString(R.string.status_late)
                }
                binding.editContentTvStatus.text = status
                dialog.hide()
            }
        }
    }

    private fun setDialogStatusRadio(priority: String, dialog: Dialog) {
        when (priority) {
            APP_ACTIVITY.getString(R.string.status_done) -> {
                dialog.findViewById<RadioButton>(R.id.dialog_status_radio_button_done).isChecked =
                    true
            }
            APP_ACTIVITY.getString(R.string.status_in_progress) -> {
                dialog.findViewById<RadioButton>(R.id.dialog_status_radio_button_in_progress).isChecked =
                    true
            }
            APP_ACTIVITY.getString(R.string.status_late) -> {
                dialog.findViewById<RadioButton>(R.id.dialog_status_radio_button_late).isChecked =
                    true
            }
        }
    }

    override fun onStop() {
        super.onStop()
        var title = binding.contentTitleText.text.toString()
        var content = binding.contentContentText.text.toString()
        val date = binding.editContentTvDate.text.toString()
        val time = binding.editContentTvTime.text.toString()
        val status = binding.editContentTvStatus.text.toString()
        val priority = binding.editContentTvPriority.text.toString()

        if (title == "") title = "title"
        if (content == "") content = "Content"

        if (newTodo) {
            val todo = Todo(
                title = title,
                content = content,
                status = status,
                priority = priority,
                date = date,
                time = time
            )
            todoViewModel.insert(todo)
        } else {
            val todo = id?.let {
                Todo(
                    id = it,
                    title = title,
                    content = content,
                    status = status,
                    priority = priority,
                    date = date,
                    time = time
                )
            }
            if (todo != null) {
                todoViewModel.update(todo)
                Log.i("UPDATE::", todo.toString())
            }
        }

        todoViewModel.allTodo.removeObservers(APP_ACTIVITY)
    }

}