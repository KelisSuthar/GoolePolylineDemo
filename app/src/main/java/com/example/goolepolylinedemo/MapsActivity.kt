package com.example.goolepolylinedemo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.goolepolylinedemo.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var polyline: Polyline? = null
    lateinit var mLoc: Location
    var locationManager: LocationManager? = null
    var drawRoute = ArrayList<LatLng>()
    var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MapsActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MapsActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MapsActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }

//        locationManager?.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER,
//            5000, 0f, this
//        );
//        if(!locationManager.(LocationManager.GPS_PROVIDER))
//        {
//            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL// to Set Map Type
        mMap.isBuildingsEnabled = true
        mMap.isIndoorEnabled = true
        mMap.moveCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    23.02587, 72.50715
                )
            )
        )

        val loc = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)//Make Sure check location enable or else return null
        }
        Log.d("NEW_LAT", loc!!.latitude.toString() + "," + loc.longitude.toString())

//        mMap.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    23.02587, 72.50715
//                ), 17.0f
//            )
//        )
        val cameraPosition = CameraPosition.Builder()
            .target(
                LatLng(
                    23.02587, 72.50715
                )
            )
            .zoom(18f)
            .tilt(90f)// To tilt Camera according Position(0-90)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        mMap.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                sydney, 15.0f
//            )
//        )

    }

    override fun onLocationChanged(location: Location) {
        Log.d("mylog", "Got Location: " + location.latitude + ", " + location.longitude);

//        mMap.clear()
        mLoc = location




        if (marker == null) {
            marker = mMap.addMarker(
                MarkerOptions().position(LatLng(location.latitude, location.longitude))
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_driver))
            )
        } else {


            MarkerAnimation().animateMarkerToGB(
                marker!!,
                LatLng(location.latitude, location.longitude),
                LatLngInterpolator.Spherical()
            )
        }
//        locationManager?.removeUpdates(this)
//        changePositionSmoothly(marker!!, LatLng(location.latitude, location.longitude), location.bearing)


//        polyline!!.remove()
//        callApi(location.latitude.toString() + ", " + location.longitude.toString())

//        Log.d(
//            "THIS_APP", getDirectionsUrl(
//                LatLng(23.012156666666666, 72.50635333333334),
//                LatLng(23.026608333333332, 72.50794166666667)
//            ).toString()
//        )

    }

    private fun callApi(originString: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl(
                "https://maps.googleapis.com/"
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI: ApiInterface = retrofit.create(ApiInterface::class.java)


        val destinationString = "23.01194, 72.50636"
        val call: Call<JsonObject?>? =
            retrofitAPI.getRouteForPolyline(
                originString,
                destinationString,
                "driving",
                resources.getString(R.string.map_key)
            )

        call?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                Log.d("RETROFIT ONSUCCESS", response.body().toString())

                val resp: JsonObject? = response.body()
                var jsonData: JSONObject? = null
                if (resp != null) {
                    jsonData = JSONObject(resp.toString())
                }

                if (jsonData != null && jsonData.has("status")) {
                    val status = jsonData.getString("status")
                    when (status) {
                        "OK" -> {
                            val jsonRoutes = jsonData.getJSONArray("routes")
                            var i = 0
                            while (i < jsonRoutes.length()) {
                                val jsonRoute = jsonRoutes.getJSONObject(i)
                                val overview_polylineJson =
                                    jsonRoute.getJSONObject("overview_polyline")

                                drawRoute.clear()
                                decodePolyLine(overview_polylineJson.getString("points"))?.forEach {
                                    drawRoute.add(it)
                                }
                                Log.e(
                                    "POINTS",
                                    drawRoute.toString()
                                )

                                i++
                            }
                        }

                    }
                    mMap.clear()
                    polyline = mMap.addPolyline(
                        PolylineOptions()
                            .clickable(true)
                            .addAll(drawRoute).width(7f).color(
                                ContextCompat.getColor(
                                    this@MapsActivity,
                                    R.color.purple_700
                                )
                            ).geodesic(true)
                    )

                    if (drawRoute.size > 1) {
                        mMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    drawRoute[0].latitude,
                                    drawRoute[0].longitude
                                )
                            )
                                .title("Current Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_loc_marker))
                                .position(LatLng(drawRoute[0].latitude, drawRoute[0].longitude))
                                .rotation(
                                    bearingBetweenLocations(
                                        LatLng(
                                            drawRoute[0].latitude,
                                            drawRoute[0].longitude
                                        ), LatLng(
                                            drawRoute[1].latitude,
                                            drawRoute[1].longitude
                                        )
                                    )
                                )
                        )
                    }

                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(drawRoute[0].latitude, drawRoute[0].longitude), 15.0f
                        )
                    )
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("RETROFIT FAIL", t.toString())
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && requestCode == 1
        ) {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100, 0f, this
            )
            return
        }


//        Log.d("THIS_APP", requestCode.toString())
//        Log.d("THIS_APP", permissions.toString())
//        Log.d("THIS_APP", grantResults.toString())
    }

    private fun decodePolyLine(poly: String): ArrayList<LatLng>? {
        val len = poly.length
        var index = 0
        val decoded: ArrayList<LatLng>? = ArrayList()
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            decoded!!.add(
                LatLng(
                    lat / 100000.0, lng / 100000.0
                )
            )
        }
        return decoded
    }

    private fun bearingBetweenLocations(latLng1: LatLng?, latLng2: LatLng?): Float {
        val PI = 3.14159
        val lat1 = latLng1!!.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2!!.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180
        val dLon = long2 - long1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon))
        var brng = Math.atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        val f1 = brng.toFloat()
        Log.e("Rotation-->", f1.toString())
        return f1
    }

    fun changePositionSmoothly(myMarker: Marker, newLatLng: LatLng, bearing: Float?) {
        val startPosition = LatLng(mLoc.latitude, mLoc.longitude)
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = 2000f
        val hideMarker = false
        handler.post(object : Runnable {
            var elapsed: Long = 0
            var t = 0f
            var v = 0f
            override fun run() {
//                myMarker.rotation = bearing!!
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed / durationInMs
                v = interpolator.getInterpolation(t)
                val currentPosition = LatLng(
                    startPosition.latitude * (1 - t) + newLatLng.latitude * t,
                    startPosition.longitude * (1 - t) + newLatLng.longitude * t
                )
                myMarker.position = currentPosition

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    myMarker.isVisible = !hideMarker
                }
                mLoc.latitude = newLatLng.latitude
                mLoc.longitude = newLatLng.longitude
            }
        })
    }
}
