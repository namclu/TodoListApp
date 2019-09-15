package com.namlu.todolistapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.namlu.todolistapp.adapters.TodoRecyclerAdapter
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.util.Constants


class TodoListActivity : AppCompatActivity(),
    TodoRecyclerAdapter.OnTodoListener,
    View.OnClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var todoRecyclerAdapter: TodoRecyclerAdapter
    lateinit var floatingActionButton: FloatingActionButton

    // Setup swipe gesture for RecyclerView
    private var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
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

    var todos = ArrayList<Todo>()

    companion object {
        const val TAG = "TodoListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        // recyclerview stuff
        recyclerView = this.findViewById(R.id.recycler_todo_list)
        initRecyclerView()
        addTodos()

        // toolbar stuff
        setSupportActionBar(findViewById(R.id.toolbar_todo_list))
        title = resources.getString(R.string.app_name)

        // fab stuff
        floatingActionButton = findViewById(R.id.fab_add_todo)
        floatingActionButton.setOnClickListener(this)
    }

    // Callback for View.OnClickListener
    override fun onClick(v: View?) {
        val intent = Intent(this, TodoDetailsActivity::class.java)
        startActivity(intent)
    }

    // Callback for TodoRecyclerAdapter.OnTodoListener
    override fun onTodoClick(position: Int) {
        val intent = Intent(this, TodoDetailsActivity::class.java)
        intent.putExtra(Constants.SELECTED_TODO_KEY, todos[position])
        startActivity(intent)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        todoRecyclerAdapter = TodoRecyclerAdapter(todos, this)
        // Attach itemTouchHelper to RecyclerView
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = todoRecyclerAdapter
    }

    // Add dummy data
    private fun addTodos() {
        for (i in 0..20) {
            val todo = Todo("Title #$i", "Content #$i", "26 Jan")
            todos.add(todo)
        }
        todoRecyclerAdapter.notifyDataSetChanged()
    }

    private fun deleteTodo(todo: Todo) {
        todos.remove(todo)
        todoRecyclerAdapter.notifyDataSetChanged()
    }
}
