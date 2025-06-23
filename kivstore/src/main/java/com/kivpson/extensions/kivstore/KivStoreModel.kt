package com.kivpson.extensions.kivstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kivpson.extensions.kivstore.providers.ContextProvider
import com.kivpson.extensions.kivstore.providers.KivStorePreferenceDelegate
import com.kivpson.extensions.kivstore.providers.StaticContextProvider
import kotlinx.coroutines.runBlocking

abstract class KivStoreModel(
    private val contextProvider: ContextProvider = StaticContextProvider
) {
    open val storeName: String
        get() = javaClass.simpleName

    private val context: Context
        get() = contextProvider.getApplicationContext()

    private val internalDataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(storeName)
        }
    }

    val dataStore: DataStore<Preferences>
        get() = internalDataStore

    fun <T> preferenceDelegate(
        key: Preferences.Key<T>,
        defaultValue: T
    ) = KivStorePreferenceDelegate(dataStore, key, defaultValue)

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    fun clearBlocking() = runBlocking {
        clear()
    }
}