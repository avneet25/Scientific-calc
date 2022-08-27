package com.knotworking.numbers.converter.history

import android.database.Cursor
import com.knotworking.numbers.converter.ConversionItem
import com.knotworking.numbers.database.DatabaseContract


object HistoryCursorConverter {
    fun getData(cursor: Cursor): List<ConversionItem> {
        //Returns an empty new MutableList.
        val historyItems: MutableList<ConversionItem> = mutableListOf()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ConversionHistory.COL_ID))
                val unitType = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ConversionHistory.COL_UNIT_TYPE_CODE))
                val inputType = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ConversionHistory.COL_INPUT_UNIT_CODE))
                val inputValue = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.ConversionHistory.COL_INPUT_VALUE))
                val outputType = cursor.getInt(cursor.getColumnIndex(DatabaseContract.ConversionHistory.COL_OUTPUT_UNIT_CODE))
                val outputValue = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.ConversionHistory.COL_OUTPUT_VALUE))
                //Adds the specified element to the end of this list.
                historyItems.add(ConversionItem(unitType, inputType, inputValue, outputType, outputValue, id))
            } while (cursor.moveToNext())
        }

        return historyItems
    }
}