package com.namlu.todolistapp.persistence

import android.content.Context
import com.namlu.todolistapp.models.Todo
import androidx.lifecycle.LiveData
import com.namlu.todolistapp.async.InsertAsyncTask


class TodoRepository constructor(context: Context){
    private var todoDatabase: TodoDatabase = TodoDatabase.getInstance(context)

    fun insertTodo(todo: Todo) {
        InsertAsyncTask(todoDatabase.getTodoDao()).execute(todo)
    }

    fun updateTodo(todo: Todo) {

    }

    fun retrieveTodos(): LiveData<List<Todo>>? {

        return todoDatabase.getTodoDao().getTodos()
    }

    fun deleteTodo(todo: Todo) {

    }
}