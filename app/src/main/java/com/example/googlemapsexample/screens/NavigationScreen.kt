package com.example.googlemapsexample.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.googlemapsexample.utils.LocationUtils
import com.example.googlemapsexample.viewmodel.LocationViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavigationScreen(
    viewModel: LocationViewModel,
    modifier: Modifier
){
    val navController = rememberNavController()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context, viewModel)
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Graph.MAIN_SCREEN
    ){
        composable(route = Graph.MAIN_SCREEN){
            MainScreen(context, locationUtils, viewModel, navController)
        }
        dialog(route = Graph.MAP_SCREEN){
            MapScreen(viewModel, locationUtils)
        }
    }
}

object Graph{
    const val MAIN_SCREEN = "MainScreen"
    const val MAP_SCREEN = "MapScreen"
}