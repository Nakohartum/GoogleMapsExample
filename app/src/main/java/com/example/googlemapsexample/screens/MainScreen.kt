package com.example.googlemapsexample.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.googlemapsexample.utils.LocationUtils
import com.example.googlemapsexample.viewmodel.LocationViewModel

@Composable
fun MainScreen(
    context: Context,
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navHostController: NavHostController
){
    val pickedLocation by viewModel.pickedLocationData.collectAsState()
    val pickedLocationAddress by viewModel.pickedLocationAddress.collectAsState()
    val userLocationData by viewModel.userLocationData.collectAsState()
    val userLocationAddress by viewModel.userLocationAddress.collectAsState()

    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            permission ->
            if (permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                && permission[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                locationUtils.getLocation()
            }
            else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity, Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                )){
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userLocationData != null && userLocationAddress == ""){
            Text("${userLocationData!!.latitude} ${userLocationData!!.longitude}")
        }
        else if (userLocationData != null && userLocationAddress != ""){
            Text(userLocationAddress)
        }
        else{
            Text("No user location")
        }
        if (pickedLocation != null && pickedLocationAddress == ""){
            Text("${pickedLocation!!.latitude} ${pickedLocation!!.longitude}")
        }
        else if (pickedLocation != null && pickedLocationAddress != ""){
            Text(pickedLocationAddress)
        }
        else{
            Text("No picked location")
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                navHostController.navigate(route = Graph.MAP_SCREEN)
            }
        ) {
            Text("Pick location")
        }
    }
}