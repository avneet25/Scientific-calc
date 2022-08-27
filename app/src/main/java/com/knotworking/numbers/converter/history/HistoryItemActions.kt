package com.knotworking.numbers.converter.history

import com.knotworking.numbers.converter.ConversionItem


interface HistoryItemActions {

    fun onItemClick(item: ConversionItem)

    fun onItemDeleteClick(item: ConversionItem): Boolean

}