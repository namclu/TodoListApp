package com.namlu.todolistapp

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.namlu.todolistapp.models.Todo
import com.namlu.todolistapp.util.Constants

class TodoDetailsActivity : AppCompatActivity(),
    View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    View.OnClickListener {

    private lateinit var editTitle: EditText
    private lateinit var textTitle: TextView
    private lateinit var editContent: EditText
    private lateinit var buttonBackArrow: ImageButton
    private lateinit var buttonCheckMark: ImageButton

    private lateinit var todo: Todo
    private var isNewTodo = false
    private lateinit var gestureDetector: GestureDetector
    private var editModeEnabled = false

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

        // Init gesture detection
        gestureDetector = GestureDetector(this, this)

        // Listen for a DoubleTap event
        editContent.setOnTouchListener(this)

        // Enable/disable edit mode from toolbar
        buttonCheckMark.setOnClickListener(this)
        textTitle.setOnClickListener(this)

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

    // Enable edit mode
    private fun enableEditMode() {
        showCheckboxButton()
        editTitle.visibility = View.VISIBLE
        textTitle.visibility = View.INVISIBLE
        editModeEnabled = true
    }

    // Disable edit mode
    private fun disableEditMode() {
        showBackArrowButton()
        editTitle.visibility = View.INVISIBLE
        textTitle.visibility = View.VISIBLE
        editModeEnabled = false
    }

    private fun showBackArrowButton() {
        buttonBackArrow.visibility = View.VISIBLE
        buttonCheckMark.visibility = View.INVISIBLE
    }

    private fun showCheckboxButton() {
        buttonBackArrow.visibility = View.INVISIBLE
        buttonCheckMark.visibility = View.VISIBLE
    }

    // Get data for existing item and set flags
    private fun getTodoIntent(): Boolean {
        if (intent.hasExtra(Constants.SELECTED_TODO_KEY)) {
            // If an existing item, get its data
            todo = intent.getParcelableExtra(Constants.SELECTED_TODO_KEY)

            editModeEnabled = false
            isNewTodo = false
        } else {
            editModeEnabled = true
            isNewTodo = true
        }
        return isNewTodo
    }

    // Set text for a new item
    private fun setNewTodoProperties() {
        editTitle.setText(R.string.new_todo)
        textTitle.text = getString(R.string.new_todo)
    }

    // Set text for an exiting item
    private fun setTodoProperties() {
        editTitle.setText(todo.title)
        textTitle.text = todo.title
        editContent.setText(todo.content)
    }
}
