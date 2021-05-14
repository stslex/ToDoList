package com.example.todolist.util

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.bd.Todo


class DiffUtilCallback(
    private val oldList: List<Todo>,
    private val newList: List<Todo>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}