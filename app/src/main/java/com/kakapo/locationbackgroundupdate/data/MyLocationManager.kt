package com.kakapo.locationbackgroundupdate.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.kakapo.locationbackgroundupdate.LocationUpdatesBroadcastReceiver
import com.kakapo.locationbackgroundupdate.hasPermission
import java.util.concurrent.TimeUnit

private const val TAG = "MyLocationManager"

@SuppressLint("UnspecifiedImmutableFlag")
class MyLocationManager private constructor(
    private val context: Context
) {
    private val _receivingLocationUpdate: MutableLiveData<Boolean> = MutableLiveData(false)
    val receivingLocationUpdate: LiveData<Boolean> get() = _receivingLocationUpdate
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest = LocationRequest().apply {
        interval = TimeUnit.SECONDS.toMillis(30)
        fastestInterval = TimeUnit.SECONDS.toMillis(50)
        maxWaitTime = TimeUnit.MINUTES.toMillis(1)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationUpdatePendingIntent: PendingIntent by lazy{
        val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @Throws(SecurityException::class)
    @MainThread
    fun startLocationUpdate(){
        Log.d(TAG, "startLocationUpdate()")

        if(!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) return

        try{
            _receivingLocationUpdate.value = true
            fusedLocationClient.requestLocationUpdates(locationRequest, locationUpdatePendingIntent)
        }catch (permissionRevoked: SecurityException){
            _receivingLocationUpdate.value = false
            Log.d(TAG, "Location permission revoked; details $permissionRevoked")
            throw permissionRevoked
        }
    }

    @MainThread
    fun stopLocationUpdates(){
        Log.d(TAG, "stopLocationUpdate")
        _receivingLocationUpdate.value = false
        fusedLocationClient.removeLocationUpdates(locationUpdatePendingIntent)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: MyLocationManager? = null

        fun getInstance(context: Context): MyLocationManager{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: MyLocationManager(context).also { INSTANCE = it }
            }
        }
    }
}
