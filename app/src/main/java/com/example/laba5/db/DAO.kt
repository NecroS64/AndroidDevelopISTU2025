package com.example.laba5


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laba5.db.Airport
import com.example.laba5.db.Favorite
import com.example.laba5.db.FlightCombination
import com.example.laba5.db.FlightCombinationDAO
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    // Поиск автозаполнения по коду или имени (ввод может быть частичным)
//    @Query("SELECT * FROM airport WHERE iata_code LIKE :query || '%' OR name LIKE :query || '%'")
//    suspend fun searchAirports(query: String): List<Airport>
    @Query("SELECT * FROM airport WHERE iata_code LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%' ORDER BY passengers DESC")
    suspend fun searchAirports(query: String): List<Airport>


    // Самые посещаемые аэропорты по убыванию
    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    suspend fun getMostVisitedAirports(): List<Airport>

    // Получение избранных рейсов
    @Query("SELECT * FROM favorite")
    suspend fun getFavoriteFlights(): List<Favorite>

    @Insert
    suspend fun Insert(favorite: Favorite)

    @Delete
    suspend fun Delete(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun deleteFavorite(departureCode: String, destinationCode: String)

    @Query("""
    SELECT 
        a1.iata_code AS from_code,
        a1.name AS from_name,
        a2.iata_code AS to_code,
        a2.name AS to_name
    FROM airport a1
    JOIN airport a2 ON a2.iata_code != a1.iata_code
    WHERE a1.iata_code = :fromCode
""")
    suspend fun getDestination(fromCode: String): List<FlightCombinationDAO>

    @Query("""
    SELECT 
        a1.iata_code AS from_code,
        a1.name AS from_name,
        a2.iata_code AS to_code,
        a2.name AS to_name
    FROM airport a1
    JOIN airport a2 ON a2.iata_code != a1.iata_code
    WHERE a1.iata_code = :fromCode
    AND a2.iata_code = :toCode
""")
    suspend fun getDestinationFavorite(fromCode: String,toCode:String): List<FlightCombinationDAO>
}

