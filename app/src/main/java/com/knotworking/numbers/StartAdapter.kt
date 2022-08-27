package com.knotworking.numbers

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

internal class StartAdapter(
        private val context: Context,
        private val typeNames: Array<String>,
        private val backColors: Array<String>
) :BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var textView: TextView
    private lateinit var backView: CardView

    override fun getCount(): Int {
        return typeNames.size
    }
    override fun getItem(position: Int): Any? {
        return null
    }
    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
    ): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.row_item, null)
        }

        textView = convertView!!.findViewById(R.id.textView)
        backView = convertView.findViewById(R.id.parent_layout)


        textView.text = typeNames[position]

        textView.setBackgroundColor(Color.parseColor(backColors[position].toString()));

        return convertView
    }
}