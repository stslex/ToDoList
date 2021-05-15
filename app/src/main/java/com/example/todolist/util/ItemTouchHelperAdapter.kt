package com.example.todolist.util

interface ItemTouchHelperAdapter {

    fun onItemMoved(fromPosition: Int, toPosition: Int)
    fun onItemSwiped(position: Int)

}