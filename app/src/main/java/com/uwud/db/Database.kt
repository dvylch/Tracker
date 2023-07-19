package com.uwud.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE_NAME = "my-location-database"

@Database(
    entities = [LocationEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocationTypeConverters::class)
abstract class MyDatabase: RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object{
        @Volatile private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): MyDatabase {
            return Room.databaseBuilder(
                context,
                MyDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}