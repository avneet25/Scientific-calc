package com.knotworking.numbers.counter

import android.databinding.BaseObservable
import android.databinding.Bindable

class CounterItem(var id: Int, // If there is a change notification of any of the listed properties,
// this value will also be considered dirty and be refreshed
                  @get:Bindable var name: String?,
                  @get:Bindable var count: Int) : BaseObservable() {
//indicates whether some other object is "equal to" this one.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CounterItem) return false

        val item = other as CounterItem?
        if (this.name != item!!.name) return false
        return if (this.count != item.count) false else id == item.id

    }
// Returns a hash code value for the object.
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
    (31 * result + count).also { result = it }
        return result
    }
}
