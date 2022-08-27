package com.knotworking.numbers.converter.history

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.knotworking.numbers.R
import com.knotworking.numbers.converter.ConversionItem
import com.knotworking.numbers.databinding.ConversionHistoryItemBinding

//The Adapter of the RecyclerView that extends RecyclerView.Adapter<YOUR-HOLDER>
class HistoryAdapter(val actions: HistoryItemActions): RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {

    private var data: List<ConversionItem> = emptyList()

    fun setData(conversionItems: List<ConversionItem>) {
        //It can be used to calculate updates for a RecyclerView Adapter
        val diffResult = DiffUtil.calculateDiff(HistoryDiffCallBack(this.data, conversionItems))
        this.data = conversionItems
        //you can swap the list with the new one then call this method to dispatch all updates to the RecyclerView.
        diffResult.dispatchUpdatesTo(this)
    }
// Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
//This new ViewHolder should be constructed with a new View that can represent the items of the given type.
// You can either create a new View manually or inflate it from an XML layout file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ConversionHistoryItemBinding>(inflater, R.layout.conversion_history_item, parent, false)
        return HistoryItemViewHolder(binding)
    }

    override fun getItemCount(): Int = data.count()
// Called by RecyclerView to display the data at the specified position.
// This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect the item at the given position.
    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.binding.viewModel = HistoryItemViewModel(data[position])
        holder.binding.actions = actions
    }

    class HistoryItemViewHolder(val binding: ConversionHistoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}