package com.namlu.todolistapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.namlu.todolistapp.R
import com.namlu.todolistapp.models.Todo
import kotlinx.android.synthetic.main.todo_list_item.view.*

class TodoRecyclerAdapter constructor(todos: ArrayList<Todo>, onTodoListener: OnTodoListener) :
    RecyclerView.Adapter<TodoRecyclerAdapter.TodoViewHolder>() {

    private val todoItems: ArrayList<Todo> = todos
    private val onTodoListener: OnTodoListener = onTodoListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false),
            onTodoListener
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        return holder.bind(todoItems[position])
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    inner class TodoViewHolder constructor(itemView: View, private val onTodoListener: OnTodoListener) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val title: TextView = itemView.text_title
        private val timeStamp: TextView = itemView.text_timestamp

        init {
            itemView.setOnClickListener(this)
            onTodoListener
        }

        // Pass the position of item clicked
        override fun onClick(v: View?) {
            onTodoListener.onTodoClick(adapterPosition)
        }

        fun bind(todo: Todo) {
            title.text = todo.title
            timeStamp.text = todo.timestamp
        }
    }

    interface OnTodoListener {
        fun onTodoClick(position: Int)
    }
}