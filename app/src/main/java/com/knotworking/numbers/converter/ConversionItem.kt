package com.knotworking.numbers.converter

// this class defines the data type of input and output values
data class ConversionItem(val unitType: Int,
                          val inputUnitCode: Int,
                          val inputValue: Double,
                          val outputUnitCode: Int,
                          val outputValue: Double,
                          val id: Int = 0)