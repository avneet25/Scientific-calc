package com.knotworking.numbers.database

import com.knotworking.numbers.converter.ConversionItem

interface DatabaseHelper {
    fun addCounterEntry(name: String)

    fun deleteCounterItem(id: Int)

    fun modifyCount(id: Int, change: Int)

    fun setCount(id: Int, value: Int)

    fun modifyName(id: Int, newName: String)

    fun saveExchangeRates(rates: Map<String, Double>)

    fun areExchangeRatesInDb(): Boolean

    fun addConversionHistoryItem(item: ConversionItem)

    fun deleteConversionHistoryItem(id: Int): Boolean
}
