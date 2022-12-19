package com.studelicious_user.ui.AddProperty.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.goolepolylinedemo.R
import com.google.android.libraries.places.api.model.AutocompletePrediction

class AutoCompleteAdapter
    (context: Context?, private val arrayList: List<AutocompletePrediction>?) :
    ArrayAdapter<AutocompletePrediction>(context!!, R.layout.dropdown, R.id.txt){
    override fun getCount(): Int {
        return arrayList!!.size
    }

    override fun getItem(position: Int): AutocompletePrediction {
        return arrayList!![position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = super.getView(position, convertView, parent)
        val item = getItem(position)
        val tvLoc1 = row.findViewById<TextView>(R.id.txt)
        if (item != null) {
            tvLoc1.text = item.getFullText(null)
        }
//        row.setOnClickListener { listener.onClick(item.getFullText(null)) }
        return row
    }
}