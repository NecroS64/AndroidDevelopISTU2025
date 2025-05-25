package com.example.laba5.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.laba5.AirportDao

@Database(entities = [Airport::class, Favorite::class], version = 15)
abstract class AppDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "flight_search.db"
                    )
                        .createFromAsset("flight_search.db")  // <- Загружаем из assets
                        //.fallbackToDestructiveMigration() // опционально
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}