package duodev.take.eazy.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseActivity
import kotlinx.android.synthetic.main.activity_return_location.*

class ReturnLocationActivity : BaseActivity() {

    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var mLat: Double = 0.0
    private var mLong: Double = 0.0
    private var address = ""
    private var reqCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_location)
        map.onCreate(savedInstanceState)
        intent?.let {
            reqCode = it.getIntExtra(CODE, 0)
        }
        init()
    }

    private fun init() {
        Log.d("MAPBT", "init")
        map.onResume()
        getLocation()
        setListeners()
    }

    private fun setListeners() {
        selectLocationButton.setOnClickListener {

            if (lat != 0.0 && long != 0.0) {

                val geocoder = Geocoder(this).getFromLocation(lat, long, 1)
                address = geocoder[0].getAddressLine(0)

                val intent = Intent()
                intent.putExtra(ADDRESS, address)
                intent.putExtra(LAT, lat)
                intent.putExtra(LONG, long)
                setResult(reqCode, intent)
                finish()

                Log.d("MAPBT", "req code = $reqCode")

            } else {
                Log.d("MAPBT", "no lat long")
                toast("select location")
            }
        }
    }

    private fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("MAPBT", "inside if")
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 12)
        } else {
            Log.d("MAPBT", "inside else")
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                Log.d("MAPBT", "location success")
                if (it != null) {
                    Log.d("MAPBT", "location non null")
                    mLat = it.latitude
                    mLong = it.longitude
                    setUpMap()
                }
            }

            LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnFailureListener {
                Log.d("MAPBT", it.toString())
            }
        }
    }

    private fun setUpMap() {
        try {
            MapsInitializer.initialize(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        map.getMapAsync {
            Log.d("MAPBT", "inside map async")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 12)
            }
            Log.d("MAPBT", "latLong: $mLat $mLong")
            it.isMyLocationEnabled = true
            val latLng = LatLng(mLat, mLong)
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(12f).build()
            it.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            it.setOnMapClickListener {latLong ->
                Log.d("MAPBT", "clicked")
                it.clear()
                it.addMarker(MarkerOptions().position(latLong))
                lat = latLong.latitude
                long = latLong.longitude
            }
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 12 && permissions.isNotEmpty()) {
            getLocation()
        }
    }

    companion object {

        const val ADDRESS = "address"
        const val LAT = "latitude"
        const val LONG = "longitude"
        private const val CODE = "code"

        fun newInstance(context: Context, req: Int) = Intent(context, ReturnLocationActivity::class.java).apply {
            putExtra(CODE, req)
        }

    }
}