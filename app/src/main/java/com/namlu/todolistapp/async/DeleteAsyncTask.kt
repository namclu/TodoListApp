package com.namlu.todolistapp.async

import android.os.AsyncTask
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.persistence.TodoDao

class DeleteAsyncTask(dao: TodoDao) : AsyncTask<Todo, Unit, Unit>() {

    private val todoDao: TodoDao

    init {
        todoDao = dao
    }

    override fun doInBackground(vararg todos: Todo?) {
        todoDao.delete(todos[0])
    }
}