package com.example.goolepolylinedemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.goolepolylinedemo.ReusedMethods.Companion.getAutocompleteData
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient


class PlacesActivity : AppCompatActivity() {
    //    var ed: EditText? = null
    var ed: AutoCompleteTextView? = null
    var placesClient: PlacesClient? = null

    var array = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        ed = findViewById(R.id.ed)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, resources.getString(R.string.map_key))
        }
        placesClient = Places.createClient(this)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.select_dialog_item, array)
        ed!!.setAdapter(adapter)
        ed!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getAutocompleteData(p0.toString(), placesClient!!) {
                    it.forEach { data ->
                        Log.e("LOC_DATA", data.getFullText(null).toString())
                        adapter.add(data.getFullText(null).toString())
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }
}