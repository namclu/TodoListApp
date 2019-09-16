package com.namlu.todolistapp.util

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    companion object{
        fun getCurrentTimestamp(): String? {
            return try {
                val simpleDateFormat = SimpleDateFormat("MMM-yyyy", Locale.getDefault())
                simpleDateFormat.format(Date())
            } catch (e: Exception) {
                null
            }
        }
    }
}