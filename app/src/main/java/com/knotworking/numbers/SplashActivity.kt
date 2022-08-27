package com.knotworking.numbers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.knotworking.numbers.api.CurrencyApi
import com.knotworking.numbers.database.DatabaseHelperImpl
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    //TODO inject singletons
    private val databaseHelper = DatabaseHelperImpl(this)
    private val currencyApi = CurrencyApi(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        // Get Currency Rate
        checkExchangeRates()

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN.toLong())
    }

    companion object {
        private const val SPLASH_SCREEN = 2000
    }


    private fun checkExchangeRates() {
        if (!databaseHelper.areExchangeRatesInDb() || isRefetchThresholdPassed()) {
            currencyApi.getExchangeRates()
        }
    }

    private fun isRefetchThresholdPassed(): Boolean {
        val fetchThreshold = TimeUnit.DAYS.toMillis(7)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lastFetchTimeStamp = sharedPreferences.getLong(Constants.EXCHANGE_RATE_FETCH_TIME, 0)
        val now = System.currentTimeMillis()
        val diff = now - lastFetchTimeStamp

        return diff > fetchThreshold
    }
}