package com.example.goolepolylinedemo

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
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

// Define a Place ID.

        }
    }
}