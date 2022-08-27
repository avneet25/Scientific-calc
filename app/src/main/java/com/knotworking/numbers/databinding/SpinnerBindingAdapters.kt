package com.knotworking.numbers.databinding

import android.databinding.BindingAdapter
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.knotworking.numbers.converter.ConverterViewModel
import com.knotworking.numbers.setSelectionSilently


object SpinnerBindingAdapters {

    @JvmStatic
    @BindingAdapter("adapter")
    fun setupSpinnerAdapter(spinner: Spinner, adapter: ArrayAdapter<CharSequence>) {
        if (spinner.adapter != adapter) {
            spinner.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter("listener")
    fun setupSpinnerChangeListener(spinner: Spinner, viewModel: ConverterViewModel) {
        if (spinner.onItemSelectedListener != viewModel) {
            Log.i("SpinnerBindingAdapters", spinner.tag.toString() + " listener set")
            spinner.onItemSelectedListener = viewModel
        }
    }

    @JvmStatic
    @BindingAdapter("selection")
    fun setSpinnerSelection(spinner: Spinner, selection: Int) {
        if (spinner.selectedItemPosition != selection) {
            Log.i("SpinnerBindingAdapters", spinner.tag.toString() + " set silently to: $selection")

            spinner.setSelectionSilently(selection)
        }
    }

}