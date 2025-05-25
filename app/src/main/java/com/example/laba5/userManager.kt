package com.example.laba5

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create an instance of the datastore at the top level of your kotlin file
val Context.myDataStore by preferencesDataStore(name = "user_prefs")

class UserManager (
    private val context: Context
) {
    // Create keys to store and retrieve the data
    companion object {
        val USER_INPUT_KEY = stringPreferencesKey("USER_INPUT")
    }

    // function to store user data
    suspend fun storeUserImput( name: String) {
        context.myDataStore.edit {
            it[USER_INPUT_KEY] = name
        }
    }

    // Create an age flow to retrieve age from the preferences


    // Create a name flow to retrieve name from the preferences
    val userInputFlow: Flow<String> = context.myDataStore.data.map {
        it[USER_INPUT_KEY] ?: ""
    }
}