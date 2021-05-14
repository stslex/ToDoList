package com.example.todolist.bd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(@PrimaryKey(autoGenerate = true) val uid: Int = 0,
                @ColumnInfo(name = "number") val number:Int,
                @ColumnInfo(name = "title") val title: String,
                @ColumnInfo(name = "content") val content: String,
                @ColumnInfo(name = "status") val status:Boolean,
                @ColumnInfo(name = "priority") val priority:String,
                @ColumnInfo(name = "date") val date:String,
                @ColumnInfo(name = "time") val time:String
)
