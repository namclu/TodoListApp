package com.namlu.todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.util.Constants

class TodoDetailsActivity : AppCompatActivity() {

    companion object{
        const val TAG = "TodoDetailsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)

        if (intent.hasExtra(Constants.SELECTED_TODO_KEY)) {
            val todo = intent.getParcelableExtra<Todo>(Constants.SELECTED_TODO_KEY)
            Log.d(TAG, "onCreate() $todo")
        }
    }
}
