package com.knotworking.numbers.database

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns



object DatabaseContract {

    val AUTHORITY = "com.knotworking.numbers"
// Immutable URI reference. A URI reference includes a URI and a fragment,
// the component of the URI following a '#'. Builds and parses URI references
    val CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY)
// basecolumn is an interface
    class Counters : BaseColumns {
        companion object {
            val TABLE = "counters"
            val COL_ID = BaseColumns._ID
            val COL_NAME = "name"
            val COL_COUNT = "count"

            val CREATE_TABLE = "CREATE TABLE " + TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_NAME + " TEXT," +
                    COL_COUNT + " INTEGER DEFAULT 0)"

            val DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE

            /**
             * The content URI for this table.
             */
            val CONTENT_URI: Uri = Uri.withAppendedPath(
                    DatabaseContract.CONTENT_URI,
                    "counters")

            /**
             * The mime type of a directory of counters.
             */
            val CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/counters"

            /**
             * The mime type of a single counter item.
             */
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/counter"
        }
    }

    class ExchangeRates : BaseColumns {
        companion object {
            val TABLE = "exchange_rates"
            val COL_ID = BaseColumns._ID
            val COL_CURRENCY = "currency"
            val COL_RATE = "rate"

            val CREATE_TABLE = "CREATE TABLE " + ExchangeRates.TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_CURRENCY + " TEXT UNIQUE," +
                    COL_RATE + " REAL)"

            val DELETE_TABLE = "DROP TABLE IF EXISTS " + ExchangeRates.TABLE

            /**
             * The content URI for this table.
             */
            val CONTENT_URI: Uri = Uri.withAppendedPath(
                    DatabaseContract.CONTENT_URI,
                    "exchange_rates")

            /**
             * The mime type of a directory of exchange rates.
             */
            val CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/exchange_rates"

            /**
             * The mime type of a single exchange rate.
             */
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/exchange_rate"
        }
    }

    class ConversionHistory : BaseColumns {
        companion object {
            val TABLE = "conversion_history"
            val COL_ID = BaseColumns._ID
            val COL_UNIT_TYPE_CODE = "type_code"
            val COL_INPUT_UNIT_CODE = "input_unit_code"
            val COL_INPUT_VALUE = "input_value"
            val COL_OUTPUT_UNIT_CODE = "output_unit_code"
            val COL_OUTPUT_VALUE = "output_value"

            val CREATE_TABLE = "CREATE TABLE " + ConversionHistory.TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_UNIT_TYPE_CODE + " INTEGER," +
                    COL_INPUT_UNIT_CODE + " INTEGER," +
                    COL_INPUT_VALUE + " REAL," +
                    COL_OUTPUT_UNIT_CODE + " INTEGER," +
                    COL_OUTPUT_VALUE + " REAL)"

            val DELETE_TABLE = "DROP TABLE IF EXISTS " + ConversionHistory.TABLE

            /**
             * The content URI for this table.
             */
            val CONTENT_URI: Uri = Uri.withAppendedPath(
                    DatabaseContract.CONTENT_URI,
                    "conversion_history")

            /**
             * The mime type of a directory of conversion history.
             */
            val CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/conversion_history"

            /**
             * The mime type of a single conversion history item.
             */
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/conversion_history"
        }
    }

}
