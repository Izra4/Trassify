package com.praktikum.trassify.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationViewModel : ViewModel() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val locationState = mutableStateOf<String>("Lokasi tidak ditemukan")
    val permissionState = mutableStateOf<Boolean>(false) // Menyimpan status izin lokasi

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    fun getLocation(context: Context) {
        // Pastikan context adalah Activity
        if (context is Activity) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            // Periksa izin terlebih dahulu
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, akses lokasi
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        locationState.value = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                    } else {
                        locationState.value = "Lokasi tidak tersedia"
                    }
                }
            } else {
                // Jika izin tidak diberikan, minta izin
                requestLocationPermission(context)
            }
        }
    }

    private fun requestLocationPermission(context: Context) {
        if (context is Activity) {
            // Menampilkan permintaan izin lokasi
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Fungsi untuk menangani hasil permintaan izin
    fun onPermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionState.value = true
            } else {
                permissionState.value = false
            }
        }
    }
}
