package com.knotworking.numbers

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.knotworking.numbers.Constants.CONVERTER_TAB
import com.knotworking.numbers.Constants.COUNTER_TAB
import com.knotworking.numbers.converter.ConverterFragment

import com.knotworking.numbers.counter.CounterFragment


class NumbersPagerAdapter(fm: FragmentManager, type: Int) : FragmentPagerAdapter(fm) {

    val typeCode = type
//Return the Fragment associated with a specified position.
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            CONVERTER_TAB -> ConverterFragment.newInstance(typeCode)
            COUNTER_TAB -> CounterFragment()
            else -> null
        }
    }
//Return the number of views available.
    override fun getCount(): Int {
        return 1
    }
}
