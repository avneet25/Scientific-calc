package com.knotworking.numbers.database

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.preference.PreferenceManager
import com.knotworking.numbers.Constants.EXCHANGE_RATE_FETCH_TIME
import com.knotworking.numbers.converter.ConversionItem

class DatabaseHelperImpl(private val context: Context) : DatabaseHelper {

    override fun addCounterEntry(name: String) {
        val contentValues = ContentValues()
        contentValues.put(DatabaseContract.Counters.COL_NAME, name)

        context.contentResolver.insert(DatabaseContract.Counters.CONTENT_URI, contentValues)
    }

    override fun deleteCounterItem(id: Int) {
        val uri = Uri.withAppendedPath(DatabaseContract.Counters.CONTENT_URI, Integer.toString(id))
        context.contentResolver.delete(uri, null, null)
    }

    override fun modifyCount(id: Int, change: Int) {
        val uri = Uri.withAppendedPath(DatabaseContract.Counters.CONTENT_URI, Integer.toString(id))

        val newCount = getItemCount(id) + change

        val values = ContentValues()
        values.put(DatabaseContract.Counters.COL_COUNT, newCount)

        context.contentResolver.update(uri, values, null, null)
    }

    override fun setCount(id: Int, value: Int) {
        val uri = Uri.withAppendedPath(DatabaseContract.Counters.CONTENT_URI, Integer.toString(id))

        val values = ContentValues()
        values.put(DatabaseContract.Counters.COL_COUNT, value)

        context.contentResolver.update(uri, values, null, null)
    }

    override fun modifyName(id: Int, newName: String) {
        val uri = Uri.withAppendedPath(DatabaseContract.Counters.CONTENT_URI, Integer.toString(id))

        val values = ContentValues()
        values.put(DatabaseContract.Counters.COL_NAME, newName)

        context.contentResolver.update(uri, values, null, null)
    }

    private fun getItemCount(id: Int): Int {
        val uri = Uri.withAppendedPath(DatabaseContract.Counters.CONTENT_URI, Integer.toString(id))
        val projection = arrayOf(DatabaseContract.Counters.COL_COUNT)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(DatabaseContract.Counters.COL_COUNT))
        }

        cursor?.close()

        return 0
    }

    override fun saveExchangeRates(rates: Map<String, Double>) {
        rates.forEach {
            replaceExchangeRate(currency = it.key, rate = it.value)
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putLong(EXCHANGE_RATE_FETCH_TIME, System.currentTimeMillis()).apply()
    }

    private fun replaceExchangeRate(currency: String, rate: Double) {
        val contentValues = ContentValues()
        contentValues.put(DatabaseContract.ExchangeRates.COL_CURRENCY, currency)
        contentValues.put(DatabaseContract.ExchangeRates.COL_RATE, rate)

        context.contentResolver.insert(DatabaseContract.ExchangeRates.CONTENT_URI, contentValues)
    }

    override fun areExchangeRatesInDb(): Boolean {
        val uri = DatabaseContract.ExchangeRates.CONTENT_URI
        val projection = arrayOf(DatabaseContract.ExchangeRates.COL_ID)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null) {
            return cursor.count > 0
        }

        cursor?.close()

        return false
    }

    override fun addConversionHistoryItem(item: ConversionItem) {
        val contentValues = ContentValues()
        contentValues.put(DatabaseContract.ConversionHistory.COL_UNIT_TYPE_CODE, item.unitType)
        contentValues.put(DatabaseContract.ConversionHistory.COL_INPUT_UNIT_CODE, item.inputUnitCode)
        contentValues.put(DatabaseContract.ConversionHistory.COL_INPUT_VALUE, item.inputValue)
        contentValues.put(DatabaseContract.ConversionHistory.COL_OUTPUT_UNIT_CODE, item.outputUnitCode)
        contentValues.put(DatabaseContract.ConversionHistory.COL_OUTPUT_VALUE, item.outputValue)

        context.contentResolver.insert(DatabaseContract.ConversionHistory.CONTENT_URI, contentValues)
    }

    override fun deleteConversionHistoryItem(id: Int): Boolean {
        val uri = Uri.withAppendedPath(DatabaseContract.ConversionHistory.CONTENT_URI, Integer.toString(id))
        return context.contentResolver.delete(uri, null, null) > 0
    }
}
