package com.namlu.todolistapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.namlu.todolistapp.R
import com.namlu.todolistapp.models.Todo
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoRecyclerAdapter constructor(todos: List<Todo>): RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder>() {

    private var todoItems: List<Todo> = todos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        return holder.bind(todoItems[position])
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    class TodoViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.text_title
        private val timeStamp: TextView = itemView.text_timestamp

        fun bind(todo: Todo) {
            title.text = todo.title
            timeStamp.text = todo.timestamp
        }
    }
}