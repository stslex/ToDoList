package com.example.todolist.bd

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {

    val allList: Flow<List<Todo>> = todoDao.getAll()

    fun getToDoItem(id: Int): Flow<Todo> = todoDao.getTodoItem(id)

    @Suppress("RedundantSuspendModifier")

    @WorkerThread
    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun deleteList(todo: List<Todo>) {
        todoDao.deleteList(todo)
    }

    suspend fun updateAll(todo: List<Todo>) {
        todoDao.updateAll(todo)
    }

}