package com.example.newsapp.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the DataStore as a singleton using an extension property on Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "SocialMediaStore")

class DatastoreManager(private val context: Context) {

    // Save a string value in the DataStore
    suspend fun saveString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // Save an integer value in the DataStore
    suspend fun saveInt(key: String, value: Int) {
        val dataStoreKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // Save a boolean value in the DataStore
    suspend fun saveBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // Retrieve a string value from the DataStore
    fun getString(key: String, defaultValue: String?): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: defaultValue
        }
    }

    // Retrieve an integer value from the DataStore
    fun getInt(key: String, defaultValue: Int): Flow<Int?> {
        val dataStoreKey = intPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: defaultValue
        }
    }

    // Retrieve a boolean value from the DataStore
    fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean?> {
        val dataStoreKey = booleanPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: defaultValue
        }
    }
}