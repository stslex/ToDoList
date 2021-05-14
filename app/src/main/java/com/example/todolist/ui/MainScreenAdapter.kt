package com.example.todolist.ui

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.bd.Todo
import com.example.todolist.databinding.DialogStatusBinding
import com.example.todolist.databinding.MainScreenItemBinding
import com.example.todolist.util.APP_ACTIVITY
import com.google.android.material.button.MaterialButton

class MainScreenAdapter : RecyclerView.Adapter<MainScreenAdapter.MainViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    open class MainViewHolder(binding: MainScreenItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.mainScreenItemTitle
        val content = binding.mainScreenItemContent
        val status = binding.mainScreenItemStatus
        val priority = binding.mainScreenItemPriority
        val date = binding.mainScreenItemDate
        val time = binding.mainScreenItemTime
    }

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
            showDialogStatus()
        }
    }

    override fun getItemCount(): Int = todoList.size

    fun addItem(todoList: MutableList<Todo>) {
        this.todoList = todoList
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