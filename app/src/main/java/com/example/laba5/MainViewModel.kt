package com.example.laba5


import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import com.example.laba5.db.Airport
import com.example.laba5.db.AppDatabase
import com.example.laba5.db.Favorite
import com.example.laba5.db.FlightCombination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch




class MainViewModel (application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application).airportDao()

    val _routes = MutableLiveData<List<FlightCombination>>()
    val routes :  LiveData<List<FlightCombination>> get() = _routes

    val _airport = MutableLiveData<List<Airport>>()
    val airport: LiveData<List<Airport>> get() = _airport

    private val _favorite = MutableLiveData<List<FlightCombination>>()
    val favorite: LiveData<List<FlightCombination>> get() = _favorite

    init {
        observeResponses()
    }

    fun getAirport(name:String){
        viewModelScope.launch {
            val airp = database.searchAirports(name)
//            for (air in airp){
//                Log.d("MyTag",air.name)
//            }

            _airport.value = airp
//            for (air in airport.value!!){
//                Log.d("MyTag",air.name)
//            }
        }
    }

    fun InsertFavorite(route:Favorite){
        viewModelScope.launch {
            Log.d("MyTag_VM","insert favorite")
            database.Insert(route)
        }
    }
    fun DeleteFavorite(route:Favorite){
        viewModelScope.launch {
            database.deleteFavorite(route.departureCode,route.destinationCode)
        }
    }
    fun getFavorite()
    {
        viewModelScope.launch {
            Log.d("MyTag_VM","get favorite")
            val routes = database.getFavoriteFlights()
            Log.d("MyTag_VM",routes.size.toString())
            val fav = mutableListOf<FlightCombination>()

            for (route in routes) {
                val result = database.getDestinationFavorite(route.departureCode, route.destinationCode)[0]
                fav.add(FlightCombination(result.from_code, result.from_name, result.to_code, result.to_name).apply {
                    isFavorite = true
                })
            }
            _favorite.value=fav.toList()
        }
    }

    fun getDestination(from:String)
    {
        viewModelScope.launch {
            val routes = database.getDestination(from)
            val withFlags = routes.map {
                FlightCombination(it.from_code, it.from_name, it.to_code, it.to_name).apply {
                    isFavorite = false
                }
            }
            _routes.value = withFlags
        }
    }

    private fun observeResponses() {
        viewModelScope.launch {

        }
    }




}

