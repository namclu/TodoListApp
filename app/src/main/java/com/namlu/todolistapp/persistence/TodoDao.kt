package com.namlu.todolistapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.namlu.todolistapp.models.Todo

@Dao
interface TodoDao {

    @Insert
    fun insert(todo: Todo?)

    @Update
    fun update(todo: Todo): Int

    @Query("SELECT * FROM todo_table")
    fun getTodos(): LiveData<List<Todo>>

    @Delete
    fun delete(todo: Todo?): Int
}