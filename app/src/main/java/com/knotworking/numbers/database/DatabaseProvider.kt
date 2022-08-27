package com.knotworking.numbers.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri


class DatabaseProvider : ContentProvider() {

    private lateinit var helper: DatabaseOpenHelper

    override fun onCreate(): Boolean {
        helper = DatabaseOpenHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var selection = selection
        var selectionArgs = selectionArgs
        val db = helper.readableDatabase
        val builder = SQLiteQueryBuilder()

        when (URI_MATCHER.match(uri)) {
            COUNTER_LIST -> builder.tables = DatabaseContract.Counters.TABLE
            COUNTER_ID -> {
                builder.tables = DatabaseContract.Counters.TABLE
                selection = DatabaseContract.Counters.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
            }
            EXCHANGE_RATE_LIST -> builder.tables = DatabaseContract.ExchangeRates.TABLE
            EXCHANGE_RATE_ID -> {
                builder.tables = DatabaseContract.ExchangeRates.TABLE
                selection = DatabaseContract.Counters.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
            }
            CONVERSION_HISTORY_LIST -> builder.tables = DatabaseContract.ConversionHistory.TABLE
            CONVERSION_HISTORY_ID -> {
                builder.tables = DatabaseContract.ConversionHistory.TABLE
                selection = DatabaseContract.ConversionHistory.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
            }
            else -> throw IllegalArgumentException("Failed to query URI: " + uri)
        }

        val cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (URI_MATCHER.match(uri)) {
            COUNTER_LIST -> DatabaseContract.Counters.CONTENT_TYPE
            COUNTER_ID -> DatabaseContract.Counters.CONTENT_ITEM_TYPE
            EXCHANGE_RATE_LIST -> DatabaseContract.ExchangeRates.CONTENT_TYPE
            EXCHANGE_RATE_ID -> DatabaseContract.ExchangeRates.CONTENT_ITEM_TYPE
            CONVERSION_HISTORY_LIST -> DatabaseContract.ConversionHistory.CONTENT_TYPE
            CONVERSION_HISTORY_ID -> DatabaseContract.ConversionHistory.CONTENT_ITEM_TYPE
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = helper.writableDatabase
        val id: Long
        id = when (URI_MATCHER.match(uri)) {
            COUNTER_LIST -> db.insertWithOnConflict(DatabaseContract.Counters.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            EXCHANGE_RATE_LIST -> db.insertWithOnConflict(DatabaseContract.ExchangeRates.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            CONVERSION_HISTORY_LIST -> db.insertWithOnConflict(DatabaseContract.ConversionHistory.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            else -> throw IllegalArgumentException("Failed to insert into URI: " + uri)
        }
        return getUriForId(id, uri)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val db = helper.writableDatabase

        val deleted: Int

        when (URI_MATCHER.match(uri)) {
            COUNTER_LIST -> deleted = db.delete(DatabaseContract.Counters.TABLE, selection, selectionArgs)
            COUNTER_ID -> {
                selection = DatabaseContract.Counters.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
                deleted = db.delete(DatabaseContract.Counters.TABLE, selection, selectionArgs)
            }
            EXCHANGE_RATE_LIST -> deleted = db.delete(DatabaseContract.ExchangeRates.TABLE, selection, selectionArgs)
            EXCHANGE_RATE_ID -> {
                selection = DatabaseContract.ExchangeRates.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
                deleted = db.delete(DatabaseContract.ExchangeRates.TABLE, selection, selectionArgs)
            }
            CONVERSION_HISTORY_LIST -> deleted = db.delete(DatabaseContract.ConversionHistory.TABLE, selection, selectionArgs)
            CONVERSION_HISTORY_ID -> {
                selection = DatabaseContract.ConversionHistory.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
                deleted = db.delete(DatabaseContract.ConversionHistory.TABLE, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Failed to delete from URI: " + uri)
        }

        if (deleted > 0) {
            context.contentResolver.notifyChange(uri, null)
        }

        return deleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var selection = selection
        var selectionArgs = selectionArgs
        val db = helper.readableDatabase
        val count: Int

        when (URI_MATCHER.match(uri)) {
            COUNTER_LIST -> count = db.update(DatabaseContract.Counters.TABLE, values, selection, selectionArgs)
            COUNTER_ID -> {
                selection = DatabaseContract.Counters.TABLE + "." + DatabaseContract.Counters.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
                count = db.update(DatabaseContract.Counters.TABLE, values, selection, selectionArgs)
            }
            EXCHANGE_RATE_LIST -> count = db.update(DatabaseContract.ExchangeRates.TABLE, values, selection, selectionArgs)
            EXCHANGE_RATE_ID -> {
                selection = DatabaseContract.ExchangeRates.TABLE + "." + DatabaseContract.ExchangeRates.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
                count = db.update(DatabaseContract.ExchangeRates.TABLE, values, selection, selectionArgs)
            }
            CONVERSION_HISTORY_LIST -> count = db.update(DatabaseContract.ConversionHistory.TABLE, values, selection, selectionArgs)
            CONVERSION_HISTORY_ID -> {
                selection = DatabaseContract.ConversionHistory.TABLE + "." + DatabaseContract.ConversionHistory.COL_ID + "=?"
                selectionArgs = arrayOf(uri.lastPathSegment)
                count = db.update(DatabaseContract.ConversionHistory.TABLE, values, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Failed to update URI: " + uri)
        }

        context.contentResolver.notifyChange(uri, null)
        return count
    }

    private fun getUriForId(id: Long, uri: Uri): Uri {
        if (id > 0) {
            val itemUri = ContentUris.withAppendedId(uri, id)
            context.contentResolver.notifyChange(itemUri, null)
            return itemUri
        } else if (id == -1L) {
            //happens when insertOnConflict IGNORE
            return uri
        }
        throw SQLException(
                "Problem while inserting into uri: " + uri)
    }

    companion object {

        private val COUNTER_LIST = 1
        private val COUNTER_ID = 2
        private val EXCHANGE_RATE_LIST = 3
        private val EXCHANGE_RATE_ID = 4
        private val CONVERSION_HISTORY_ID = 5
        private val CONVERSION_HISTORY_LIST = 6

        private val URI_MATCHER: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {

            URI_MATCHER.addURI(DatabaseContract.AUTHORITY,
                    "counters",
                    COUNTER_LIST)
            URI_MATCHER.addURI(DatabaseContract.AUTHORITY,
                    "counters/*",
                    COUNTER_ID)

            URI_MATCHER.addURI(DatabaseContract.AUTHORITY,
                    "exchange_rates",
                    EXCHANGE_RATE_LIST)
            URI_MATCHER.addURI(DatabaseContract.AUTHORITY,
                    "exchange_rates/*",
                    EXCHANGE_RATE_ID)

            URI_MATCHER.addURI(DatabaseContract.AUTHORITY,
                    "conversion_history",
                    CONVERSION_HISTORY_LIST)
            URI_MATCHER.addURI(DatabaseContract.AUTHORITY,
                    "conversion_history/*",
                    CONVERSION_HISTORY_ID)
        }
    }
}
