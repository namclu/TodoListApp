package com.namlu.todolistapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.namlu.todolistapp.models.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "todo_db"
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return instance as TodoDatabase
        }
    }

    abstract fun getTodoDao(): TodoDao
}