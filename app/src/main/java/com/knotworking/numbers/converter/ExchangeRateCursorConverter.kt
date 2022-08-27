package com.knotworking.numbers.converter

import android.database.Cursor
import com.knotworking.numbers.database.DatabaseContract


object ExchangeRateCursorConverter {
    fun getData(cursor: Cursor): Map<String, Double> {
        //A modifiable collection that holds pairs of objects (keys and values)
        // and supports efficiently retrieving the value corresponding to each key.
        val exchangeRates: MutableMap<String, Double> = mutableMapOf()

        if (cursor.moveToFirst()) {
            do {
                val currency = cursor.getString(cursor.getColumnIndex(DatabaseContract.ExchangeRates.COL_CURRENCY))
                val rate = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.ExchangeRates.COL_RATE))

               // the previous value associated with the key, or null if the key was not present in the map.
                exchangeRates.put(currency, rate)
            } while (cursor.moveToNext())
        }

        return exchangeRates
    }
}