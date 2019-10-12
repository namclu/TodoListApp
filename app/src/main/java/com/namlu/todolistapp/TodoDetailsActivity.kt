package com.namlu.todolistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.persistence.TodoRepository
import com.namlu.todolistapp.util.Constants
import com.namlu.todolistapp.util.DateTimeUtil
import kotlinx.android.synthetic.main.activity_todo_details.*
import kotlinx.android.synthetic.main.toolbar_todo_details.*


class TodoDetailsActivity : AppCompatActivity(),
    View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    TextWatcher {

    private lateinit var initialTodo: Todo
    private lateinit var finalTodo: Todo
    private var isNewTodo = false
    private lateinit var gestureDetector: GestureDetector
    private var editModeEnabled = false
    private lateinit var todoRepository: TodoRepository

    companion object {
        val TAG: String = TodoDetailsActivity::class.java.simpleName

        fun intentLaunchCreateTodo(context: Context) {
            val intent = Intent(context, TodoDetailsActivity::class.java)
            context.startActivity(intent)
        }

        fun intentLaunchEditTodo(context: Context, todo: Todo) {
            val intent = Intent(context, TodoDetailsActivity::class.java).apply {
                putExtra(Constants.SELECTED_TODO_KEY, todo)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)

        // Handle config changes
        if (savedInstanceState != null) {
            editModeEnabled = savedInstanceState.getBoolean("editMode")
            if (editModeEnabled) {
                enableEditMode()
            }
        }

        // Init other classes
        gestureDetector = GestureDetector(this, this)
        todoRepository = TodoRepository(this)

        // Set listeners
        edit_content_todo_details.setOnTouchListener(this)
        edit_title_toolbar.addTextChangedListener(this)

        // Init an item
        if (getTodoIntent()) {
            // New item, enter Edit mode
            setNewTodoProperties()
            enableEditMode()
        } else {
            // Existing item, enter View mode
            setTodoProperties()
        }

        setupToolbar()
    }

    // Callback for View.OnTouchListener
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    // Callbacks for GestureDetector.OnGestureListener
    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    // Callbacks for GestureDetector.OnDoubleTapListener
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        enableEditMode()
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    // Callbacks for TextWatcher
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        text_title_toolbar.text = s.toString()
    }

    // If in edit mode, intercept back pressed and exit out of edit mode
    // Else when not in edit mode, back pressed will dismiss screen as per usual
    override fun onBackPressed() {
        if (editModeEnabled) {
            button_check_mark_toolbar.setOnClickListener {
                disableEditMode()
                return@setOnClickListener
            }
        }

        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("editMode", editModeEnabled)
    }

    // Enable/disable edit mode
    // We're in "View" mode
    private fun disableEditMode() {
        showBackArrowButton()
        edit_title_toolbar.visibility = View.INVISIBLE
        text_title_toolbar.visibility = View.VISIBLE
        editModeEnabled = false

        // Check if anything was typed into the item. Don't save if title or content is empty
        var temp = edit_content_todo_details.text.toString()
        temp = temp.replace("\n", "")
        temp = temp.replace(" ", "")
        if (temp.isNotEmpty()) {
            finalTodo.title = edit_title_toolbar.text.toString()
            finalTodo.content = edit_title_toolbar.text.toString()
            finalTodo.timestamp = DateTimeUtil.getCurrentTimestamp()

            // If the note was altered, save it.
            if (!finalTodo.content.equals(initialTodo.content) || !finalTodo.title.equals(initialTodo.title)) {
                saveChanges()
            }
        }
    }

    // We're in edit mode, can save changes
    private fun enableEditMode() {
        showCheckboxButton()
        edit_title_toolbar.visibility = View.VISIBLE
        text_title_toolbar.visibility = View.INVISIBLE
        editModeEnabled = true
    }

    // Get data for an item and set flags
    // Unfortunately method only works when we're clicking an item from list view.
    private fun getTodoIntent(): Boolean {
        if (intent.hasExtra(Constants.SELECTED_TODO_KEY)) {
            // If an existing item, get its data
            initialTodo = intent.getParcelableExtra(Constants.SELECTED_TODO_KEY)
            finalTodo = Todo(initialTodo.id, initialTodo.title, initialTodo.content, initialTodo.timestamp)

            editModeEnabled = false
            isNewTodo = false
            Log.d(TAG, "getTodoIntent() isNewTodo $isNewTodo")
        } else {
            editModeEnabled = true
            isNewTodo = true
            Log.d(TAG, "getTodoIntent() isNewTodo $isNewTodo")
        }
        return isNewTodo
    }

    private fun showBackArrowButton() {
        button_back_arrow_toolbar.visibility = View.VISIBLE
        button_check_mark_toolbar.visibility = View.INVISIBLE
    }

    private fun showCheckboxButton() {
        button_back_arrow_toolbar.visibility = View.INVISIBLE
        button_check_mark_toolbar.visibility = View.VISIBLE
    }

    // Save either a new item or an existing item
    private fun saveChanges() {
        when (isNewTodo) {
            true -> saveNewTodo()
            else -> updateTodo()
        }
    }

    private fun saveNewTodo() {
        todoRepository.insertTodo(finalTodo)
    }

    private fun updateTodo() {
        todoRepository.updateTodo(finalTodo)
    }

    // Set text for a new item
    private fun setNewTodoProperties() {
        edit_title_toolbar.setText("")
        text_title_toolbar.text = ""

        initialTodo = Todo()
        finalTodo = Todo()
    }

    // Set text for an exiting item
    private fun setTodoProperties() {
        edit_title_toolbar.setText(initialTodo.title)
        text_title_toolbar.text = initialTodo.title
        edit_content_todo_details.setText(initialTodo.content)
    }

    // Setup toolbar click listeners
    private fun setupToolbar() {
        // Setup check mark
        button_check_mark_toolbar.setOnClickListener {
            disableEditMode()
        }

        // Setup back arrow
        button_back_arrow_toolbar.setOnClickListener {
            finish()
        }

        // Setup text title
        text_title_toolbar.setOnClickListener {
            enableEditMode()
            edit_title_toolbar.requestFocus()
            edit_title_toolbar.setSelection(edit_title_toolbar.length())
        }
    }
}
