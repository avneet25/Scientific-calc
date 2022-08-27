package com.knotworking.numbers.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DatabaseContract.Counters.CREATE_TABLE)
        db.execSQL(DatabaseContract.ExchangeRates.CREATE_TABLE)
        db.execSQL(DatabaseContract.ConversionHistory.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.i(TAG, "Upgrading db from version $oldVersion to $newVersion")
    }

    companion object {
        private val TAG = DatabaseOpenHelper::class.java.simpleName

        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Numbers.db"
    }
}
