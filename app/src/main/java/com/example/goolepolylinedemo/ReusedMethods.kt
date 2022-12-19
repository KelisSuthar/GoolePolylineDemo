package com.example.goolepolylinedemo

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.studelicious_user.ui.AddProperty.Adapter.AutoCompleteAdapter
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class ReusedMethods {
    companion object {
        fun getAutocomplete(
            constraint: CharSequence,
            placesClient: PlacesClient
        ): List<AutocompletePrediction?>? {


            //Create a RectangularBounds object.
            val bounds = RectangularBounds.newInstance(
                LatLng(-33.880490, 151.184363),
                LatLng(-33.858754, 151.229596)
            )
            val requestBuilder = FindAutocompletePredictionsRequest.builder()
                .setQuery(constraint.toString())
//                .setCountry("") //Use only in specific country
                // Call either setLocationBias() OR setLocationRestriction().
//                .setLocationBias(bounds) //                        .setLocationRestriction(bounds)
                .setSessionToken(AutocompleteSessionToken.newInstance())
//                .setTypeFilter(TypeFilter.GEOCODE)
            val results: Task<FindAutocompletePredictionsResponse?> =
                placesClient.findAutocompletePredictions(requestBuilder.build())


            //Wait to get results.
            try {
                Tasks.await(results, 60, TimeUnit.SECONDS)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            Log.e("PLACE_DATA", results.isSuccessful.toString())
            Log.e("PLACE_DATA", results.isComplete.toString())
            return if (results.isSuccessful) {
                if (results.result != null) {
                    results.result!!.autocompletePredictions
                } else null

            } else {
                null
            }
        }

        fun getAutocompleteData(
            text: String,
            placesClient: PlacesClient,
            callback: (List<AutocompletePrediction>) -> Unit,
        ) {
            val token = AutocompleteSessionToken.newInstance()
//            val bounds = RectangularBounds.newInstance(
//                LatLng(-33.880490, 151.184363),  //dummy lat/lng
//                LatLng(-33.858754, 151.229596)
//            )
            val request =
                FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(token)
                    .setQuery(text)
                    .build()
            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                callback(response.autocompletePredictions)
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    val apiException = exception
                    Log.e("LOC_DATA", "Place not found: " + apiException.statusCode)
                }
            }
        }

        fun AutoCompleteTextView.setGoogleAutoComplete(context: Context) {
            var autoCompleteAdapter: AutoCompleteAdapter? = null
            val array = ArrayList<AutocompletePrediction>()
            if (!Places.isInitialized()) {
                Places.initialize(context, context.resources.getString(R.string.map_key))
            }
            val placesClient = Places.createClient(context)
            this.threshold = 1
            this.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, pos, id ->
                Log.e("##########", autoCompleteAdapter!!.getItem(pos).getFullText(null).toString())
                this.setText(autoCompleteAdapter!!.getItem(pos).getFullText(null))
            }
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    getAutocompleteData(p0.toString(), placesClient) {
                        array.clear()
                        it.forEach {
                            array.add(it)
                        }
                        autoCompleteAdapter!!.notifyDataSetChanged()
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            autoCompleteAdapter = AutoCompleteAdapter(context, array)
            this.setAdapter(autoCompleteAdapter)
        }
    }
}