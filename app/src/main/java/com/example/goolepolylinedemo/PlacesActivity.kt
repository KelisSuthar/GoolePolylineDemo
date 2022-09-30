package com.example.goolepolylinedemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class PlacesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var array = ArrayList<String>()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, resources.getString(R.string.map_key))
        }
        val placesClient = Places.createClient(this)
        Thread {
            // a potentially time consuming task
            ReusedMethods.getAutocomplete("Gujarat", placesClient)!!.forEach {
                Log.e("PLACES_DATA_FULL", it!!.getFullText(null).toString())
//                Log.e("PLACES_DATA_PRI", it.getPrimaryText(null).toString())
//                Log.e("PLACES_DATA_SEC", it.getSecondaryText(null).toString())
//                Log.e("PLACES_DATA_SEC", it.placeTypes.toString())
//                Log.e("PLACES_DATA_SEC", it.distanceMeters.toString())
//                val placeId = it.placeId

// Specify the fields to return.
                val placeFields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG)

// Construct a request object, passing the place ID and fields array.
//                val request = FetchPlaceRequest.newInstance(placeId, placeFields)
//                placesClient.fetchPlace(request)
//                    .addOnSuccessListener { response: FetchPlaceResponse ->
//
//                        Log.e("PLACES_DATA_LAT", response.place.latLng!!.toString())
//                        Log.e("PLACES_DATA_LAT", response.place.latLng!!.toString())
//                        Log.e("PLACES_DATA_LAT", response.place.latLng!!.toString())
//                    }.addOnFailureListener { exception: Exception ->
//                        if (exception is ApiException) {
////                        Log.e(TAG, "Place not found: ${exception.message}")
//                            val statusCode = exception.statusCode
//                        }
//                    }
//

            }
        }.start()

    }

}