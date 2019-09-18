package com.namlu.todolistapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.namlu.todolistapp.adapters.TodoRecyclerAdapter
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.persistence.TodoRepository
import com.namlu.todolistapp.util.Constants
import com.namlu.todolistapp.util.VertSpacingItemDecorator


class TodoListActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var todoRecyclerAdapter: TodoRecyclerAdapter
    lateinit var floatingActionButton: FloatingActionButton
    lateinit var todoRepository: TodoRepository

    var todos = ArrayList<Todo>()

    companion object {
        val TAG: String = TodoListActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        setupRecyclerTodoList()
        setupToolbar()
        setupFab()
    }

    private fun deleteTodo(todo: Todo) {
        todoRepository.deleteTodo(todo)
        todoRecyclerAdapter.notifyDataSetChanged()
    }

    private fun fetchTodos() {
        todoRepository.retrieveTodos()?.observe(this,
            Observer<List<Todo>> { t ->
                if (todos.size > 0) {
                    todos.clear()
                }
                if (!t.isNullOrEmpty()) {
                    todos.addAll(t)
                }
                todoRecyclerAdapter.notifyDataSetChanged()
            })
    }

    private fun initRecyclerTodoList() {
        recyclerView = this.findViewById(R.id.recycler_todo_list)

        todoRecyclerAdapter = TodoRecyclerAdapter(todos, object: TodoRecyclerAdapter.OnTodoListener{
            override fun onTodoClick(position: Int) {
                val intent = Intent(this@TodoListActivity, TodoDetailsActivity::class.java).apply {
                    putExtra(Constants.SELECTED_TODO_KEY, todos[position])
                }
                startActivity(intent)
            }

        })
        recyclerView.adapter = todoRecyclerAdapter

        // Attach itemTouchHelper to RecyclerView
        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
                // Only using onSwiped() atm
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteTodo(todos[viewHolder.adapterPosition])
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(VertSpacingItemDecorator(24))
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupFab() {
        floatingActionButton = findViewById(R.id.fab_add_todo)
        floatingActionButton.setOnClickListener {
            val intent = Intent(this@TodoListActivity, TodoDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerTodoList() {
        initRecyclerTodoList()
        todoRepository = TodoRepository(this)
        fetchTodos()
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar_todo_list))
        title = resources.getString(R.string.app_name)
    }
}
