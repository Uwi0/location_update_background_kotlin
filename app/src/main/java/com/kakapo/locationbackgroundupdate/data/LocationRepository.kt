package com.kakapo.locationbackgroundupdate.data

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import com.kakapo.locationbackgroundupdate.data.db.MyLocationDatabase
import com.kakapo.locationbackgroundupdate.data.db.MyLocationEntity
import java.util.*
import java.util.concurrent.ExecutorService

class LocationRepository private constructor(
    private val myLocationDatabase: MyLocationDatabase,
    private val myLocationManager: MyLocationManager,
    private val executor: ExecutorService
) {

    private val locationDao = myLocationDatabase.locationDao()
    val receivedLocationUpdates: LiveData<Boolean> = myLocationManager.receivingLocationUpdate

    fun getLocations(): LiveData<List<MyLocationEntity>> = locationDao.getLocations()

    fun getLocationId(id: UUID): LiveData<MyLocationEntity> = locationDao.getLocation(id)

    fun updateLocation(myLocationEntity: MyLocationEntity){
        executor.execute{
            locationDao.updateLocation(myLocationEntity)
        }
    }

    fun addLocation(myLocationEntity: MyLocationEntity){
        executor.execute{
            locationDao.addLocation(myLocationEntity)
        }
    }

    @MainThread
    fun startLocationUpdates() = myLocationManager.startLocationUpdate()

    @MainThread
    fun stopLocationUpdates() = myLocationManager.stopLocationUpdates()

    companion object{
        @Volatile private var INSTANCE: LocationRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): LocationRepository{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: LocationRepository(
                    MyLocationDatabase.getInstance(context),
                    MyLocationManager.getInstance(context),
                    executor
                )
                    .also { INSTANCE = it }
            }
        }
    }
}