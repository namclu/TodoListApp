package com.namlu.todolistapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namlu.todolistapp.adapters.TodoRecyclerAdapter
import com.namlu.todolistapp.models.Todo


class TodoListActivity : AppCompatActivity(), TodoRecyclerAdapter.OnTodoListener {

    lateinit var recyclerView: RecyclerView
    lateinit var todoRecyclerAdapter: TodoRecyclerAdapter

    var todos = ArrayList<Todo>()

    companion object{
        const val TAG = "TodoListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        // recyclerview stuff
        recyclerView = this.findViewById(R.id.recycler_todo)
        initRecyclerView()
        addTodos()

        // toolbar stuff
        setSupportActionBar(findViewById(R.id.toolbar))
        title = resources.getString(R.string.app_name)
    }

    override fun onTodoClick(position: Int) {
        Log.d(TAG, "onTodoClick: clicked #$position")
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        todoRecyclerAdapter = TodoRecyclerAdapter(todos, this)
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
