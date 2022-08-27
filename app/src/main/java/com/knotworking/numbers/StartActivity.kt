package com.knotworking.numbers

import android.app.Notification.EXTRA_TITLE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.knotworking.numbers.Constants.EXCHANGE_RATE_FETCH_TIME
import com.knotworking.numbers.api.CurrencyApi
import com.knotworking.numbers.database.DatabaseHelperImpl
import java.util.*
import java.util.concurrent.TimeUnit

class StartActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var gridContainer: LinearLayout
    private lateinit var loadTextContainer: LinearLayout
    private var typeNames = arrayOf("Weight", "Temperature", "Length", "Volumn", "Power", "Currency")
    private var typeIcons = arrayOf("#ffd740","#15DB18", "#FF0000", "#F34F4F", "#2E6EB9", "#4A50B9")


    //TODO inject singletons
    private val databaseHelper = DatabaseHelperImpl(this)
    private val currencyApi = CurrencyApi(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)



        // App Bar
        setSupportActionBar(findViewById(R.id.toolbar))

        //home navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "Converter"

        // Navigation Grid View
        gridView = findViewById(R.id.grid_view)
        val mainAdapter = StartAdapter(this@StartActivity, typeNames, typeIcons)
        gridView.adapter = mainAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            Log.e("CONVERT", "WAT=> Position: $position" )

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, position)
                putExtra(EXTRA_TITLE, typeNames[position])
            }
            startActivity(intent)
        }

        // Hide Gride as first
        gridContainer = findViewById(R.id.grid_container)
        gridContainer.visibility = View.INVISIBLE

        // Show Loading Text
        loadTextContainer = findViewById(R.id.loadTextContainer)
        loadTextContainer.visibility = View.VISIBLE

        // Get Currency Rate
        checkExchangeRates()
        val handler = Handler()
        handler.postDelayed({
            // Hide Load text and Navigation Grid
            loadTextContainer.visibility = View.INVISIBLE
            gridContainer.visibility = View.VISIBLE
        }, 2000)



    }

    private fun checkExchangeRates() {
        if (!databaseHelper.areExchangeRatesInDb() || isRefetchThresholdPassed()) {
            currencyApi.getExchangeRates()
        }
    }

    private fun isRefetchThresholdPassed(): Boolean {
        val fetchThreshold = TimeUnit.DAYS.toMillis(7)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lastFetchTimeStamp = sharedPreferences.getLong(EXCHANGE_RATE_FETCH_TIME, 0)
        val now = System.currentTimeMillis()
        val diff = now - lastFetchTimeStamp

        return diff > fetchThreshold
    }
}
