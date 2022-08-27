package com.knotworking.numbers.converter

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.knotworking.numbers.R
import com.knotworking.numbers.Utils
import com.knotworking.numbers.converter.history.HistoryAdapter
import com.knotworking.numbers.converter.history.HistoryItemActions
import com.knotworking.numbers.database.DatabaseHelper
import com.knotworking.numbers.database.DatabaseHelperImpl


//TODO issue, go from currency history item to temp history item


class ConverterViewModel(private val fragment: ConverterFragment, typeCode: Int) :
        BaseObservable(), HistoryItemActions, AdapterView.OnItemSelectedListener {
// setting required array adapters to all conversions
    lateinit var typeAdapter: ArrayAdapter<CharSequence>
    private lateinit var massAdapter: ArrayAdapter<CharSequence>
    private lateinit var temperatureAdapter: ArrayAdapter<CharSequence>
    private lateinit var distanceAdapter: ArrayAdapter<CharSequence>
    private lateinit var volumnAdapter: ArrayAdapter<CharSequence>
    private lateinit var powerAdapter: ArrayAdapter<CharSequence>
    private lateinit var currencyAdapter: ArrayAdapter<CharSequence>

    //TODO observable fields or base observable?
// baseObservable class is A class that implements the Observable interface allows the
// registration of listeners that want to be notified of property changes of on the observable object.
    var unitType: ObservableField<Int> = ObservableField(typeCode)


    var inputUnitCode: ObservableField<Int> = ObservableField(UnitCode.MASS_KG)

    var inputValue: ObservableField<Double> = ObservableField(1.0)

    var outputUnitCode: ObservableField<Int> = ObservableField(UnitCode.MASS_G)

    var outputValue: ObservableField<Double> = ObservableField(0.0)

    val context: Context = fragment.context!!

    //TODO inject singletons

    private val databaseHelper: DatabaseHelper = DatabaseHelperImpl(context) // creating database for the unit conversions
    // INotifyPropertyChanged needs to be raised by the object whose properties are changing.
    //You can use this adapter to provide views for an AdapterView , Returns a view for each object in a collection of data
    // objects you provide, and can be used with list-based user interface widgets such as ListView  or Spinner .
    var inputOutputAdapter: ArrayAdapter<CharSequence>? = null
        @Bindable get() = field //If there is a change notification of any of the listed properties,
        // this value will also be considered outdated and be refreshed.
        set(value) {
            field = value
            notifyPropertyChanged(BR.inputOutputAdapter) //Notifies listeners that a specific property has changed.
        }
   //
    val historyAdapter = HistoryAdapter(this)
//A collection that holds pairs of objects (keys and values) and supports efficiently retrieving the value corresponding to each key.
// Map keys are unique; the map holds only one value for each key
    var exchangeRates: Map<String, Double> = emptyMap()
//its methods will be called when the text is changed
    lateinit var inputEditTextWatcher: TextWatcher
    lateinit var outputEditTextWatcher: TextWatcher

    init {
        setupAdapters()
        when(typeCode) { // arranging the unit into input output adapter accordingly
            UnitCode.TYPE_MASS-> {
                inputOutputAdapter = massAdapter
            }
            UnitCode.TYPE_VOLUMN-> {
                inputOutputAdapter = volumnAdapter
            }
            UnitCode.TYPE_POWER-> {
                inputOutputAdapter = powerAdapter
            }
            UnitCode.TYPE_CURRENCY-> {
                inputOutputAdapter = currencyAdapter
            }
            UnitCode.TYPE_DISTANCE-> {
                inputOutputAdapter = distanceAdapter
            }
            UnitCode.TYPE_TEMPERATURE-> {
                inputOutputAdapter = temperatureAdapter
            }
            else -> {
            inputOutputAdapter = massAdapter
            }
        }

        setupEditTextWatchers()
    }
    // creating all adapter arrays for every unit converter and currency
// You can use this adapter to provide views for an AdapterView , Returns a view for each object in a collection of data objects you provide,
// and can be used with list-based user interface widgets such as ListView  or Spinner .
    private fun setupAdapters() {
        typeAdapter = ArrayAdapter.createFromResource(context, R.array.type_array, android.R.layout.simple_spinner_item)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        massAdapter = ArrayAdapter.createFromResource(context, R.array.mass_array, android.R.layout.simple_spinner_item)
        massAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        temperatureAdapter = ArrayAdapter.createFromResource(context, R.array.temperature_array, android.R.layout.simple_spinner_item)
        temperatureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        distanceAdapter = ArrayAdapter.createFromResource(context, R.array.distance_array, android.R.layout.simple_spinner_item)
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        volumnAdapter = ArrayAdapter.createFromResource(context, R.array.volumn_array, android.R.layout.simple_spinner_item)
        volumnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        powerAdapter = ArrayAdapter.createFromResource(context, R.array.power_array, android.R.layout.simple_spinner_item)
        powerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencyAdapter = ArrayAdapter.createFromResource(context, R.array.currency_array, android.R.layout.simple_spinner_item)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    private fun setupEditTextWatchers() {
        //When an object of this type is attached to an Editable, its methods will be called when the text is changed.
        inputEditTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//Returns a string representation of the object
                val value = Utils.getDoubleFromString(s.toString())
                // Set the stored value.
                inputValue.set(value)

                calculateConversion(value)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }

        outputEditTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val value = Utils.getDoubleFromString(s.toString())
                outputValue.set(value)
                calculateConversion(value, true)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }
    }

    private fun calculateConversion(changedValue: Double, outputChanged: Boolean = false) {
        // displaying information in logs
        Log.i("TAG", "Calculate conversion. OutputChanged: $outputChanged")
        Log.i("TAG", "Calculate conversion. input value: $changedValue")

        var input = changedValue

        val inputCode: Int = if (outputChanged) {
            outputUnitCode.get()!!
        } else {
            inputUnitCode.get()!!
        }

        val outputCode: Int = if (outputChanged) {
            inputUnitCode.get()!!
        } else {
            outputUnitCode.get()!!
        }

        var output = 0.00000000

        when (unitType.get()) {  // gets input and changes output
            UnitCode.TYPE_MASS -> {
                input = Utils.toGrams(inputCode, input)
                output = Utils.fromGrams(outputCode, input)
            }
            UnitCode.TYPE_TEMPERATURE -> {
//                output = if (inputCode == outputCode) {
//                    input
//                } else {
//                    if (inputCode == UnitCode.TEMP_C) Utils.toFahrenheit(input)
//                    else Utils.toCelsius(input)
//                }
                input = Utils.toCelsius(inputCode, input)
                output = Utils.fromCelsius(outputCode, input)
            }
            UnitCode.TYPE_DISTANCE -> {
                input = Utils.toMetres(inputCode, input)
                output = Utils.fromMetres(outputCode, input)
            }
            UnitCode.TYPE_VOLUMN -> {
                input = Utils.toCubicMeter(inputCode, input)
                output = Utils.fromCubicMeter(outputCode, input)
            }
            UnitCode.TYPE_POWER -> {
                input = Utils.toWat(inputCode, input)
                output = Utils.fromWat(outputCode, input)
            }
            UnitCode.TYPE_CURRENCY -> {
                input = Utils.toUsd(inputCode, input, exchangeRates)
                output = Utils.fromUsd(outputCode, input, exchangeRates)
            }
        }

        if (outputChanged) {
            inputValue.set(output)
        } else {
            outputValue.set(output)
        }
    }

    fun saveCurrentItem() {
        val conversionItem = ConversionItem(unitType.get()!!,
                inputUnitCode.get()!!,
                inputValue.get()!!,
                outputUnitCode.get()!!,
                outputValue.get()!!)

        databaseHelper.addConversionHistoryItem(conversionItem)

        Utils.hideKeyboard(fragment.view!!)
    }

    fun setConversionItem(item: ConversionItem) {
        handleTypeSelected(item.unitType)

        unitType.set(item.unitType)
        inputUnitCode.set(item.inputUnitCode)
        inputValue.set(item.inputValue)
        outputUnitCode.set(item.outputUnitCode)
        outputValue.set(item.outputValue)
    }

    override fun onItemClick(item: ConversionItem) {
        fragment.loadHistoryItem(item) // it brings up the unit that user selected
        Utils.hideKeyboard(fragment.view!!)
    }

    override fun onItemDeleteClick(item: ConversionItem): Boolean {
        //delete the item from data base that is created
        return databaseHelper.deleteConversionHistoryItem(item.id)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
// this method lets the user interaction to position the item at certain position within spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            R.id.fragment_converter_type_spinner -> {
                handleTypeSelected(position)
            }
            R.id.fragment_converter_input_spinner -> {
                inputUnitCode.set(position)
            }
            R.id.fragment_converter_output_spinner -> {
                outputUnitCode.set(position)
            }
        }
// this method will convert the values to the required unit converter
        calculateConversion(inputValue.get()!!)
    }
// when user selects the position of an item on spinner then the corresponding adapter is used
    private fun handleTypeSelected(position: Int) {
        unitType.set(position)

        when (position) {
            UnitCode.TYPE_MASS -> {
                Log.i("ConverterViewModel", "Mass selected")
                inputOutputAdapter = massAdapter
                inputUnitCode.set(UnitCode.MASS_G)
                outputUnitCode.set(UnitCode.MASS_OZ)
            }
            UnitCode.TYPE_TEMPERATURE -> {
                Log.i("ConverterViewModel", "Temp selected")
                inputOutputAdapter = temperatureAdapter
                inputUnitCode.set(UnitCode.TEMP_C)
                outputUnitCode.set(UnitCode.TEMP_F)
            }
            UnitCode.TYPE_DISTANCE -> {
                Log.i("ConverterViewModel", "Dist selected")
                inputOutputAdapter = distanceAdapter
                inputUnitCode.set(UnitCode.DIST_MI)
                outputUnitCode.set(UnitCode.DIST_KM)
            }
            UnitCode.TYPE_VOLUMN -> {
                Log.i("ConverterViewModel", "Volumn selected")
                inputOutputAdapter = volumnAdapter
                inputUnitCode.set(UnitCode.VOL_M)
                outputUnitCode.set(UnitCode.VOL_CM)
            }
            UnitCode.TYPE_POWER -> {
                Log.i("ConverterViewModel", "Power selected")
                inputOutputAdapter = powerAdapter
                inputUnitCode.set(UnitCode.POWER_W)
                outputUnitCode.set(UnitCode.POWER_KW)
            }
            UnitCode.TYPE_CURRENCY -> {
                Log.i("ConverterViewModel", "$$$ selected")

                if (exchangeRates.isEmpty()) {  // if it unable to extract information using api for some reasons
                    Toast.makeText(context, R.string.empty_exchange_rates, Toast.LENGTH_SHORT).show()
                    handleTypeSelected(UnitCode.TYPE_MASS)
                } else {
                    inputOutputAdapter = currencyAdapter
                    inputUnitCode.set(UnitCode.USD)
                    outputUnitCode.set(UnitCode.USD)
                }

            }
        }
    }

}