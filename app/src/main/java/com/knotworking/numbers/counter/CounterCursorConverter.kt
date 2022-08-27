package com.knotworking.numbers.counter

import android.database.Cursor

import com.knotworking.numbers.database.DatabaseContract

import java.util.ArrayList


object CounterCursorConverter {

    fun getData(cursor: Cursor): List<CounterItem> {
        val counterItems = ArrayList<CounterItem>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Counters.COL_ID))
                val entryName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Counters.COL_NAME))
                val entryCount = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Counters.COL_COUNT))
                counterItems.add(CounterItem(id, entryName, entryCount))
            } while (cursor.moveToNext())
        }

        return counterItems
    }

}
