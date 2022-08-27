package com.knotworking.numbers

import android.content.Context
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Spinner
import com.knotworking.numbers.converter.UnitCode
import com.knotworking.numbers.converter.UnitCode.CAD
import com.knotworking.numbers.converter.UnitCode.CNY
import com.knotworking.numbers.converter.UnitCode.DIST_CM
import com.knotworking.numbers.converter.UnitCode.DIST_F
import com.knotworking.numbers.converter.UnitCode.DIST_KM
import com.knotworking.numbers.converter.UnitCode.DIST_M
import com.knotworking.numbers.converter.UnitCode.DIST_MI
import com.knotworking.numbers.converter.UnitCode.EUR
import com.knotworking.numbers.converter.UnitCode.GBP
import com.knotworking.numbers.converter.UnitCode.HKD
import com.knotworking.numbers.converter.UnitCode.INR
import com.knotworking.numbers.converter.UnitCode.KPW
import com.knotworking.numbers.converter.UnitCode.MASS_G
import com.knotworking.numbers.converter.UnitCode.MASS_KG
import com.knotworking.numbers.converter.UnitCode.MASS_LBS
import com.knotworking.numbers.converter.UnitCode.MASS_OZ
import com.knotworking.numbers.converter.UnitCode.NZD
import com.knotworking.numbers.converter.UnitCode.POWER_GW
import com.knotworking.numbers.converter.UnitCode.POWER_KW
import com.knotworking.numbers.converter.UnitCode.POWER_MW
import com.knotworking.numbers.converter.UnitCode.POWER_W
import com.knotworking.numbers.converter.UnitCode.TEMP_C
import com.knotworking.numbers.converter.UnitCode.TEMP_F
import com.knotworking.numbers.converter.UnitCode.TEMP_K
import com.knotworking.numbers.converter.UnitCode.USD
import com.knotworking.numbers.converter.UnitCode.VOL_CM
import com.knotworking.numbers.converter.UnitCode.VOL_L
import com.knotworking.numbers.converter.UnitCode.VOL_M
import com.knotworking.numbers.converter.UnitCode.VOL_ML
import com.knotworking.numbers.converter.UnitCode.VOL_MM

// set the value without triggering the listener
fun Spinner.setSelectionSilently(position: Int) {
    val listener = this.onItemSelectedListener
    this.onItemSelectedListener = null
    this.setSelection(position, false)
    this.onItemSelectedListener = listener
}

// set the value without triggering the listener
fun EditText.setTextSilently(text: String, watcher: TextWatcher) {
    this.removeTextChangedListener(watcher)
    this.setText(text)
    this.addTextChangedListener(watcher)
}

object Utils {
    fun toGrams(inputUnitCode: Int, input: Double): Double {
        return when (inputUnitCode) {
            MASS_KG -> input * 1000
            MASS_G -> input
            MASS_LBS -> input * 453.59237
            MASS_OZ -> input * 28.34952
            else -> 0.0
        }
    }

    fun fromGrams(outputUnitCode: Int, grams: Double): Double {
        return when (outputUnitCode) {
            MASS_KG -> grams / 1000
            MASS_G -> grams
            MASS_LBS -> grams / 453.59237
            MASS_OZ -> grams / 28.34952
            else -> 0.0
        }
    }



    fun toCelsius(inputUnitCode: Int, input: Double): Double {
        return when (inputUnitCode) {
            TEMP_C -> input
            TEMP_F -> (input - 32) / 1.8
            TEMP_K -> input + 273.15
            else -> 0.0
        }
    }

    fun fromCelsius(outputUnitCode: Int, output: Double): Double {
        return when (outputUnitCode) {
            TEMP_C -> output
            TEMP_F -> output*1.8 + 32
            TEMP_K -> output - 273.15
            else -> 0.0
        }
    }

//    fun fromCelsius(celsius: Double): Double {
//        return celsius + 273.15
//    }

    fun toMetres(inputUnitCode: Int, input: Double): Double {
        return when (inputUnitCode) {
            DIST_MI -> input * 1609.344
            DIST_F -> input * 0.3048
            DIST_KM -> input * 1000
            DIST_M -> input
            DIST_CM -> input / 100
            else -> 0.0
        }
    }

    fun fromMetres(outputUnitCode: Int, metres: Double): Double {
        return when (outputUnitCode) {
            DIST_MI -> metres / 1609.344
            DIST_F -> metres / 0.3048
            DIST_KM -> metres / 1000
            DIST_M -> metres
            DIST_CM -> metres * 100
            else -> 0.0
        }
    }

    // ------------------- Volumn ------------
    fun fromCubicMeter (outputUnitCode: Int, cubicMeter: Double): Double {
        return when (outputUnitCode) {

            VOL_CM -> cubicMeter * 1000000.0
            VOL_MM -> cubicMeter * 1000000000.0
            VOL_L -> cubicMeter * 1000.0
            VOL_ML -> cubicMeter * 1000000.0
            VOL_M -> cubicMeter
            else -> 0.0
        }
    }

    fun toCubicMeter (inputUnitCode: Int, cubicMeter: Double): Double {
        return when (inputUnitCode) {

            VOL_CM -> cubicMeter / 1000000.0
            VOL_MM -> cubicMeter / 1000000000.0
            VOL_L -> cubicMeter / 1000.0
            VOL_ML -> cubicMeter / 1000000.0
            VOL_M -> cubicMeter
            else -> 0.0
        }
    }

    // ------------------- Power ------------
    fun fromWat (outputUnitCode: Int, wat: Double): Double {
        Log.e("CONVERT", "WAT=> outputUnitCode: $outputUnitCode , Wat: $wat")
        return when (outputUnitCode) {
            POWER_KW -> wat / 1000
            POWER_MW -> wat / 1000000
            POWER_GW -> wat / 1000000000
            POWER_W -> wat
            else -> 0.0
        }
    }

    fun toWat (inputUnitCode: Int, wat: Double): Double {
        Log.e("CONVERT", "WAT=> inputUnitCode: $inputUnitCode , Wat: $wat")

        return when (inputUnitCode) {
            POWER_KW -> wat * 1000
            POWER_MW -> wat * 1000000
            POWER_GW -> wat * 1000000000
            POWER_W -> wat
            else -> 0.0
        }
    }


    fun toUsd(inputUnitCode: Int, input: Double, rates: Map<String, Double>): Double {
        val exchangeRate = getExchangeRate(inputUnitCode, rates)
        return input / exchangeRate
    }

    fun fromUsd(outputUnitCode: Int, usd: Double, rates: Map<String, Double>): Double {
        val exchangeRate = getExchangeRate(outputUnitCode, rates)
        return usd * exchangeRate
    }

    private fun getExchangeRate(currencyCode: Int, rates: Map<String, Double>): Double {
        return when (currencyCode) {
            EUR -> rates.getValue(Constants.EUR)
            GBP -> rates.getValue(Constants.GBP)
            USD -> rates.getValue(Constants.USD)
            CAD -> rates.getValue(Constants.CAD)
            INR -> rates.getValue(Constants.INR)
            HKD -> rates.getValue(Constants.HKD)
            NZD -> rates.getValue(Constants.NZD)
            KPW -> rates.getValue(Constants.KPW)
            CNY -> rates.getValue(Constants.CNY)

            else -> 0.0
        }
    }

    fun round(input: Double): Double {
//        return (input * 100).roundToInt() / 100.0
        return input
    }

    fun getDoubleFromString(floatString: String): Double {
        return try {
            java.lang.Double.valueOf(floatString)!!
        } catch (e: Exception) {
            0.0
        }

    }

    //TODO use enums
    fun getUnitSymbol(context: Context, typeCode: Int, unitCode: Int): String {
        val typeArray = when (typeCode) {
            UnitCode.TYPE_MASS -> R.array.mass_array
            UnitCode.TYPE_TEMPERATURE -> R.array.temperature_array
            UnitCode.TYPE_DISTANCE -> R.array.distance_array
            UnitCode.TYPE_VOLUMN -> R.array.volumn_array
            UnitCode.TYPE_POWER -> R.array.power_array
            UnitCode.TYPE_CURRENCY -> R.array.currency_array
            else -> 0
        }

        val spacing = if (typeCode != UnitCode.TYPE_TEMPERATURE) " " else ""

        return spacing + context.resources.getStringArray(typeArray)[unitCode]
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
