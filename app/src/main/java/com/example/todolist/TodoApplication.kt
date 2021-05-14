package com.example.todolist

import android.app.Application
import com.example.todolist.bd.TodoRepository
import com.example.todolist.bd.TodoRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TodoApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { TodoRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TodoRepository(database.todoDao()) }
}