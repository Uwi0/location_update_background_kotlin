package com.kakapo.locationbackgroundupdate.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MyLocationEntity::class], version = 1)
@TypeConverters(MyLocationTypeConverter::class)
abstract class MyLocationDatabase : RoomDatabase() {

    abstract fun locationDao(): MyLocationDao

    companion object{

        private const val DATABASE_NAME = "my-location-database"

        @Volatile
        private var INSTANCE: MyLocationDatabase? = null

        fun getInstance(context: Context): MyLocationDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): MyLocationDatabase{
            return Room.databaseBuilder(
                context,
                MyLocationDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}