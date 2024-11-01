package com.example.googlemapsexample.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.googlemapsexample.viewmodel.LocationViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.Locale

class LocationUtils(
    private val context: Context,
    private val viewModel: LocationViewModel
) {
    private val _fusedProvider = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(1000).build()
    private val userLocationCallback = object: LocationCallback(){
        @SuppressLint("NewApi")
        override fun onLocationResult(location: LocationResult) {
            super.onLocationResult(location)
            viewModel.updateUserLocation(location.lastLocation)
            getAddressFromLocation(viewModel)
        }
    }
    private fun isLocationPermissionGranted(): Boolean{
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        if (isLocationPermissionGranted()){
            _fusedProvider.requestLocationUpdates(
                locationRequest, userLocationCallback, Looper.getMainLooper()
            )
        }
    }

    fun stopGettingLocation(){
        _fusedProvider.removeLocationUpdates(userLocationCallback)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getAddressFromLocation(viewModel: LocationViewModel){
        val locationData = viewModel.userLocationData.value
        if (locationData != null){
            val geocoder = Geocoder(context, Locale.getDefault())
            geocoder.getFromLocation(
                locationData.latitude, locationData.longitude, 1,
                (Geocoder.GeocodeListener { addresses ->
                    val address = addresses[0].getAddressLine(0)
                    if (address != null){
                        viewModel.setUserAddress(address)
                    }
                })
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getAddressFromPickedLocation(viewModel: LocationViewModel){
        val locationData = viewModel.pickedLocationData.value
        if (locationData != null){
            val geocoder = Geocoder(context, Locale.getDefault())
            geocoder.getFromLocation(
                locationData.latitude, locationData.longitude, 1

            ) { addresses ->
                val address = addresses[0].getAddressLine(0)
                if (address != null) {
                    viewModel.setPickedLocationAddress(address)
                }
            }
        }
    }

}