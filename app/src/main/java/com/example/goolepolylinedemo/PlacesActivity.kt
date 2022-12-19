package com.example.goolepolylinedemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.goolepolylinedemo.ReusedMethods.Companion.getAutocompleteData
import com.example.goolepolylinedemo.ReusedMethods.Companion.setGoogleAutoComplete
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.studelicious_user.ui.AddProperty.Adapter.AutoCompleteAdapter


class PlacesActivity : AppCompatActivity() {
    //    var ed: EditText? = null
    var ed: AutoCompleteTextView? = null
    var placesClient: PlacesClient? = null
    var autoCompleteAdapter: AutoCompleteAdapter? = null

    var array = ArrayList<AutocompletePrediction>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
                ed = findViewById(R.id.ed)
        ed!!.setGoogleAutoComplete(this)

//        if (!Places.isInitialized()) {
//            Places.initialize(applicationContext, resources.getString(R.string.map_key))
//        }
//        placesClient = Places.createClient(this)
//        ed!!.threshold = 1
//        ed!!.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, pos, id ->
//            Log.e("##########", autoCompleteAdapter!!.getItem(pos).getFullText(null).toString())
//            ed!!.setText(autoCompleteAdapter!!.getItem(pos).getFullText(null))
//        }
//        ed!!.addTextChangedListener(object : TextWatcher {
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//                getAutocompleteData(p0.toString(), placesClient!!) {
//                    array.clear()
//                    it.forEach {
//                        array.add(it)
//                    }
//                    autoCompleteAdapter!!.notifyDataSetChanged()
//                }
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {}
//        })
//        autoCompleteAdapter = AutoCompleteAdapter(this, array)
//        ed!!.setAdapter(autoCompleteAdapter)
    }
}