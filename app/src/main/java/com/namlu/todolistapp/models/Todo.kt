package com.namlu.todolistapp.models

import android.os.Parcel
import android.os.Parcelable

data class Todo(
    var title: String?,
    var content: String?,
    var timestamp: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(title)
        parcel?.writeString(content)
        parcel?.writeString(timestamp)
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<Todo> {
        override fun createFromParcel(parcel: Parcel): Todo {
            return Todo(parcel)
        }

        override fun newArray(size: Int): Array<Todo?> {
            return arrayOfNulls(size)
        }
    }
}