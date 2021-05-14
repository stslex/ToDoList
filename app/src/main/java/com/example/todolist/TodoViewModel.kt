package com.example.todolist

import android.util.Log
import androidx.lifecycle.*
import com.example.todolist.bd.Todo
import com.example.todolist.bd.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val allTodo: LiveData<List<Todo>> = repository.allList.asLiveData()

    fun getToDoItem(id: Int) = repository.getToDoItem(id).asLiveData()

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun update(todo: Todo) = viewModelScope.launch {
        Log.i("Update:", " ${todo.id}")
        repository.update(todo)
    }

    fun updateAll(todo: List<Todo>) = viewModelScope.launch {
        Log.i("UpdateAll:", " ${todo[0].id}")
        repository.updateAll(todo)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun deleteList(todo: List<Todo>) = viewModelScope.launch {
        repository.deleteList(todo)
    }
}

class TodoViewModelFactory(private val repository: TodoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}