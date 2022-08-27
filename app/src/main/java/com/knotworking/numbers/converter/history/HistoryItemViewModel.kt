package com.knotworking.numbers.converter.history

import android.content.Context
import com.knotworking.numbers.Utils
import com.knotworking.numbers.converter.ConversionItem


class HistoryItemViewModel(val item: ConversionItem) {

    fun getInputDisplayText(context: Context): String {
        return getDisplayText(context, item.inputValue, item.inputUnitCode)
    }

    fun getOutputDisplayText(context: Context): String {
        return getDisplayText(context, item.outputValue, item.outputUnitCode)
    }

    private fun getDisplayText(context: Context, value: Double, unitCode: Int): String {
        val displayValue = Utils.round(value).toString()
        val unit = Utils.getUnitSymbol(context, item.unitType, unitCode)
        return displayValue + unit
    }

}