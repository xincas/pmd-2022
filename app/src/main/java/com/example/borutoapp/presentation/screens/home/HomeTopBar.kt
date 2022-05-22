package com.example.borutoapp.presentation.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.borutoapp.R
import com.example.borutoapp.ui.theme.topAppBarBackgroundColor
import com.example.borutoapp.ui.theme.topAppBarContentColor
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

val isFlashOn = mutableStateOf(value = false)

fun flashlight(context: Context, flashlightState: Boolean) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    cameraManager.setTorchMode(cameraManager.cameraIdList[0], flashlightState)
}

lateinit var fusedLocationClient: FusedLocationProviderClient

@SuppressLint("MissingPermission")
@Composable
fun HomeTopBar(onSearchClicked: () -> Unit) {

    val context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)


    TopAppBar(
        title = {
            Text(
                text = "Explore",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            IconButton(onClick = {
                isFlashOn.value = !isFlashOn.value
                flashlight(context, isFlashOn.value)
            }) {
                Icon(
                    imageVector = if (isFlashOn.value) Icons.Default.FlashOff else Icons.Default.FlashOn,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
            IconButton(onClick = {
                fusedLocationClient.lastLocation.addOnSuccessListener {

                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    val address = addresses[0].getAddressLine(0)

                    Toast.makeText(
                        context,
                        "lat = ${it.latitude} lng = ${it.longitude}\n" +
                                address,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
        }
    )
}

@Composable
@Preview
fun HomeTopBarPreview() {
    HomeTopBar {}
}
