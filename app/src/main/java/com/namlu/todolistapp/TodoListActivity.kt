package com.namlu.todolistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TodoListActivity : AppCompatActivity() {

    companion object{
        const val TAG = "TodoListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)
    }
}
