package com.kakapo.locationbackgroundupdate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kakapo.locationbackgroundupdate.data.LocationRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class LocationUpdateViewModel(application: Application): AndroidViewModel(application) {

    private val locationRepository = LocationRepository.getInstance(
        application.applicationContext,
        Executors.newSingleThreadExecutor()
    )

    val receivingLocationUpdate: LiveData<Boolean> = locationRepository.receivedLocationUpdates

    val locationListLiveData = locationRepository.getLocations()

    fun startLocationUpdates() = locationRepository.startLocationUpdates()

    fun stopLocationUpdate() = locationRepository.stopLocationUpdates()
}