package com.knotworking.numbers.databinding

import android.databinding.BindingAdapter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.knotworking.numbers.Utils
import com.knotworking.numbers.setTextSilently

object EditTextBindingAdapters {
//Specifies that an additional static method needs to be generated from this element if it's a function.
    @JvmStatic
    //BindingAdapter is applied to methods that are used to manipulate how values with expressions are set to views.
    @BindingAdapter("listener")
    fun setupEditTextListener(editText: EditText, listener: TextWatcher) {
        editText.addTextChangedListener(listener)
    }

    @JvmStatic
    @BindingAdapter(value = ["value", "watcher"], requireAll = true)
    fun setText(editText: EditText, value: Double, watcher: TextWatcher) {
        val newValue = Utils.round(value)
        val oldValue = Utils.getDoubleFromString(editText.text.toString())
        if (newValue != oldValue) {
            Log.i("EditTextBindingAdapters", editText.tag.toString() + " set silently to: $newValue")
            editText.setTextSilently(newValue.toString(), watcher)
        }
    }
}