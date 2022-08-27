package com.knotworking.numbers.counter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.knotworking.numbers.R
import com.knotworking.numbers.databinding.CounterItemBinding

//used to display data for a specific adapter position may be placed in a cache
// for later reuse to display the same type of data again later.
class CounterAdapter(context: Context) : RecyclerView.Adapter<CounterAdapter.ViewHolder>() {

    private var data: List<CounterItem> = emptyList()
    // creating an object of counter action impl
    private val counterActions: CounterActionsImpl = CounterActionsImpl(context)

    fun setData(counterItems: List<CounterItem>?) {
        var counterItems = counterItems
        if (counterItems == null) {
            counterItems = emptyList()
        }
 //DiffUtil is a utility class that can calculate the difference between two lists and output a list of update operations that converts the first list into the second one.
        // It can be used to calculate updates for a RecyclerView Adapter
//Calculates the list of update operations that can covert one list into the other one.
        val diffResult = DiffUtil.calculateDiff(CounterDiffCallback(this.data, counterItems))
        this.data = counterItems
        //Dispatches the update events to the given adapter.
        diffResult.dispatchUpdatesTo(this)
    }
// Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val counterItemBinding = DataBindingUtil.inflate<CounterItemBinding>(LayoutInflater.from(parent.context), R.layout.counter_item, parent, false)
        return ViewHolder(counterItemBinding)
    }
//  Called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindCounterItem(item, counterActions)
    }
// Returns the total number of items in the data set held by the adapter.
    override fun getItemCount(): Int {
        return data.size
    }
// Return the stable ID for the item at position.
    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

//root is the outermost View in the layout file associated with the Binding.
    class ViewHolder(private var itemBinding: CounterItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindCounterItem(item: CounterItem, actions: CounterActions) {
            itemBinding.item = item
            itemBinding.actions = actions
            itemBinding.executePendingBindings()
        }
    }

}
