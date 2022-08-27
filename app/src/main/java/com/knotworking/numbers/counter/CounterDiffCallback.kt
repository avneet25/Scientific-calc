package com.knotworking.numbers.counter

import android.support.v7.util.DiffUtil


class CounterDiffCallback(private val oldList: List<CounterItem>, private val newList: List<CounterItem>) : DiffUtil.Callback() {
//Returns the size of the old list.
    override fun getOldListSize(): Int {
        return oldList.size
    }
//Returns the size of the new list.
    override fun getNewListSize(): Int {
        return newList.size
    }
//Called by the DiffUtil to decide whether two object represent the same Item
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
//Called by the DiffUtil when it wants to check whether two items have the same data.
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}
