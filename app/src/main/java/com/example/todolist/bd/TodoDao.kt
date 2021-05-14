package com.example.todolist.bd

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table")
    fun getAll(): Flow<List<Todo>>

    @Insert
    suspend fun insert(todo: Todo)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(todo: Todo)

    @Delete
    suspend fun deleteList(notes: List<Todo>)

    @Update(entity = Todo::class)
    suspend fun update(todo: Todo)

}