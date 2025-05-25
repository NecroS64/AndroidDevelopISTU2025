package com.example.laba5.db
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey



@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val iata_code: String,
    val passengers: Int
)

data class FlightCombination(
    val from_code: String,
    val from_name: String,
    val to_code: String,
    val to_name: String,
    var isFavorite: Boolean=false
)


data class FlightCombinationDAO(
    val from_code: String,
    val from_name: String,
    val to_code: String,
    val to_name: String
)


@Entity(tableName = "favorite")
data class Favorite(
    @ColumnInfo(name = "departure_code")
    val departureCode: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "destination_code")
    val destinationCode: String
)




