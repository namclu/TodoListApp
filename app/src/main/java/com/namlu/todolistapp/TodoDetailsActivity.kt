package com.namlu.todolistapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.persistence.TodoRepository
import com.namlu.todolistapp.util.Constants
import com.namlu.todolistapp.util.DateTimeUtil


class TodoDetailsActivity : AppCompatActivity(),
    View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    View.OnClickListener,
    TextWatcher{

    private lateinit var editTitle: EditText
    private lateinit var textTitle: TextView
    private lateinit var editContent: EditText
    private lateinit var buttonBackArrow: ImageButton
    private lateinit var buttonCheckMark: ImageButton

    private lateinit var initialTodo: Todo
    private lateinit var finalTodo: Todo
    private var isNewTodo = false
    private lateinit var gestureDetector: GestureDetector
    private var editModeEnabled = false
    private lateinit var todoRepository: TodoRepository

    companion object {
        const val TAG = "TodoDetailsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_details)

        // Init views
        editTitle = findViewById(R.id.edit_todo_details_title)
        textTitle = findViewById(R.id.text_todo_details_title)
        editContent = findViewById(R.id.edit_todo_details)
        buttonBackArrow = findViewById(R.id.button_back_arrow)
        buttonCheckMark = findViewById(R.id.button_check_mark)

        // Init other classes
        gestureDetector = GestureDetector(this, this)
        todoRepository = TodoRepository(this)

        // Set listeners
        editContent.setOnTouchListener(this)
        buttonCheckMark.setOnClickListener(this)
        textTitle.setOnClickListener(this)
        editTitle.addTextChangedListener(this)

        // Back arrow takes user back to RecyclerView
        buttonBackArrow.setOnClickListener(this)

        // Init an item
        if (getTodoIntent()) {
            // New item, enter Edit mode
            setNewTodoProperties()
            enableEditMode()
        } else {
            // Existing item, enter View mode
            setTodoProperties()
        }
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

    // Callback for View.OnClickListener
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.button_check_mark -> {
                    disableEditMode()
                }
                    R.id.text_todo_details_title -> {
                    enableEditMode()
                    editTitle.requestFocus()
                    editTitle.setSelection(editTitle.length())
                }
                R.id.button_back_arrow -> {
                    finish()
                }
            }
        }
    }

    // Callbacks for TextWatcher
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textTitle.text = s.toString()
    }

    // If in edit mode, intercept back pressed and exit out of edit mode
    // Else when not in edit mode, back pressed will dismiss screen as per usual
    override fun onBackPressed() {
        if (editModeEnabled) {
            onClick(buttonCheckMark)
        } else {
            super.onBackPressed()
        }
    }

    // Handle config changes
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            editModeEnabled = savedInstanceState.getBoolean("editMode")
            if (editModeEnabled) {
                enableEditMode()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("editMode", editModeEnabled)
    }

    // Enable/disable edit mode
    // We're in "View" mode
    private fun disableEditMode() {
        showBackArrowButton()
        editTitle.visibility = View.INVISIBLE
        textTitle.visibility = View.VISIBLE
        editModeEnabled = false

        // Check if anything was typed into the item. Don't save if title or content is empty
        var temp = editContent.getText().toString()
        temp = temp.replace("\n", "")
        temp = temp.replace(" ", "")
        if (temp.isNotEmpty()) {
            finalTodo.title = editTitle.text.toString()
            finalTodo.content = editContent.text.toString()
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
        editTitle.visibility = View.VISIBLE
        textTitle.visibility = View.INVISIBLE
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
        buttonBackArrow.visibility = View.VISIBLE
        buttonCheckMark.visibility = View.INVISIBLE
    }

    private fun showCheckboxButton() {
        buttonBackArrow.visibility = View.INVISIBLE
        buttonCheckMark.visibility = View.VISIBLE
    }

    // Save either a new item or an existing item
    private fun saveChanges() {
        when(isNewTodo) {
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
        editTitle.setText("")
        textTitle.text = ""

        initialTodo = Todo()
        finalTodo = Todo()
    }

    // Set text for an exiting item
    private fun setTodoProperties() {
        editTitle.setText(initialTodo.title)
        textTitle.text = initialTodo.title
        editContent.setText(initialTodo.content)
    }
}
