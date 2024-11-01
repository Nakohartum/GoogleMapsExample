package com.example.googlemapsexample.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.googlemapsexample.utils.LocationUtils
import com.example.googlemapsexample.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnrememberedMutableState")
@Composable
fun MapScreen(viewModel: LocationViewModel, locationUtils: LocationUtils){
    val pickedLocation by viewModel.pickedLocationData.collectAsState()
    val userLocation by viewModel.userLocationData.collectAsState()
    val latLng = if (userLocation == null) LatLng(0.0,0.0) else LatLng(userLocation!!.latitude, userLocation!!.longitude)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(latLng, 10f)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                viewModel.updatePickedLocation(it)
                locationUtils.getAddressFromPickedLocation(viewModel)
            }
        ){
            val tempLatLng = if (pickedLocation == null) latLng else LatLng(pickedLocation!!.latitude, pickedLocation!!.longitude)
            Marker(
                state = MarkerState(
                    position = tempLatLng
                )
            )
        }
    }
}