package com.knotworking.numbers.counter


// interface which implements the following functions
interface CounterActions {
    fun itemLongClick(item: CounterItem): Boolean

    fun incrementCount(id: Int)

    fun decrementCount(id: Int)
}
