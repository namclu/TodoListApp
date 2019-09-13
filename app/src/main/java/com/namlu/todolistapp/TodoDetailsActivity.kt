package com.namlu.todolistapp

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.util.Constants

class TodoDetailsActivity : AppCompatActivity() {

    private lateinit var editTitle: EditText
    private lateinit var textTitle: TextView
    private lateinit var editContent: EditText

    private lateinit var todo: Todo
    private var isNewTodo = false

    companion object {
        const val TAG = "TodoDetailsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)

        editTitle = findViewById(R.id.edit_todo_details_title)
        textTitle = findViewById(R.id.text_todo_details_title)
        editContent = findViewById(R.id.edit_todo_details)

        if (isNewTodo()) {
            // New item so enter Edit mode
            setNewTodoProperties()
        } else {
            // Not a new item so enter View mode
            setTodoProperties()
        }
    }

    private fun isNewTodo(): Boolean {
        if (intent.hasExtra(Constants.SELECTED_TODO_KEY)) {
            // If item is not new, get its data
            todo = intent.getParcelableExtra(Constants.SELECTED_TODO_KEY)

            isNewTodo = false
        } else {
            isNewTodo = true
        }

        return isNewTodo
    }

    // Set text for a new item
    private fun setNewTodoProperties() {
        editTitle.setText(R.string.new_todo)
        textTitle.text = getString(R.string.new_todo)
    }

    // Set text for a
    private fun setTodoProperties() {
        editTitle.setText(todo.title)
        textTitle.text = todo.title
        editContent.setText(todo.content)
    }
}
