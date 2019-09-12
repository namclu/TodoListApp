package com.namlu.todolistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namlu.todolistapp.adapters.TodoRecyclerAdapter
import com.namlu.todolistapp.models.Todo


class TodoListActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var todoRecyclerAdapter: TodoRecyclerAdapter

    var todos = ArrayList<Todo>()

    companion object{
        const val TAG = "TodoListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        recyclerView = this.findViewById(R.id.recycler_todo)
        initRecyclerView()
        addTodos()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        todoRecyclerAdapter = TodoRecyclerAdapter(todos)
        recyclerView.adapter = todoRecyclerAdapter
    }

    private fun addTodos() {
        for (i in 0..20) {
            val todo = Todo("Title #$i", "Content #$i", "26 Jan")
            todos.add(todo)
        }
        todoRecyclerAdapter.notifyDataSetChanged()
    }
}
