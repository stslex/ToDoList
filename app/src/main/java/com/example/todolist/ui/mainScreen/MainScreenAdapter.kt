package com.example.todolist.ui.mainScreen

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.TodoViewModel
import com.example.todolist.bd.Todo
import com.example.todolist.databinding.MainScreenItemBinding
import com.example.todolist.util.APP_ACTIVITY
import com.example.todolist.util.DiffUtilCallback
import com.google.android.material.button.MaterialButton


class MainScreenAdapter(private val todoViewModel: TodoViewModel) :
    RecyclerView.Adapter<MainScreenAdapter.MainViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = MainScreenItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.status.setOnClickListener {
            showDialogStatus(position)
        }
        holder.layout.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("NewTodo", false)
            bundle.putInt("TodoId", todoList[position].id)
            findNavController(holder.itemView).navigate(
                R.id.action_mainScreenFragment_to_contentEditFragment,
                bundle
            )
        }
        Log.i("size", todoList.size.toString())
        Log.i("position", position.toString())
        holder.bind(todoList[position])

    }

    fun addItem(todoListInput: MutableList<Todo>) {
        val mDiffResult = DiffUtil.calculateDiff(DiffUtilCallback(todoList, todoListInput))
        mDiffResult.dispatchUpdatesTo(this)
        todoList = todoListInput
    }

    private fun showDialogStatus(position: Int) {
        val dialog = Dialog(APP_ACTIVITY)
        dialog.setContentView(R.layout.dialog_status)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dr_item_recycler)
        dialog.show()

        val buttonSave = dialog.findViewById<MaterialButton>(R.id.dialog_status_button_save)
        buttonSave.setOnClickListener {
            var status = ""
            val radioGroup = dialog.findViewById<RadioGroup>(R.id.dialog_status_radio_group)
            when (radioGroup.checkedRadioButtonId) {
                R.id.dialog_status_radio_button_in_progress -> status =
                    APP_ACTIVITY.getString(R.string.status_in_progress)
                R.id.dialog_status_radio_button_done -> status =
                    APP_ACTIVITY.getString(R.string.status_done)
                R.id.dialog_status_radio_button_late -> status =
                    APP_ACTIVITY.getString(R.string.status_late)
            }

            val todo = Todo(
                id = todoList[position].id,
                title = todoList[position].title,
                content = todoList[position].content,
                status = status,
                priority = todoList[position].priority,
                date = todoList[position].date,
                time = todoList[position].time
            )

            todoViewModel.update(todo)

            dialog.hide()
        }
    }

    override fun getItemCount(): Int = todoList.size

    class MainViewHolder(binding: MainScreenItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.mainScreenItemTitle
        private val content = binding.mainScreenItemContent
        private val priority = binding.mainScreenItemPriority
        private val date = binding.mainScreenItemDate
        private val time = binding.mainScreenItemTime
        val status = binding.mainScreenItemStatus
        val layout = binding.mainScreenItemLayout

        fun bind(todoList: Todo) {
            title.text = todoList.title
            content.text = todoList.content
            status.text = todoList.status
            priority.text = todoList.priority
            date.text = todoList.date
            time.text = todoList.time
        }
    }
}
