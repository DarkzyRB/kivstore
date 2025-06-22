package com.kivpson.extensions.kivstore.providers

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.flow.first

class KivStorePreferenceDelegate<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
) : ReadWriteProperty<Any, T> {

    @Volatile
    private var cachedValue: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return cachedValue ?: runBlocking {
            val prefs = dataStore.data.first()
            prefs[key] ?: defaultValue
        }.also { cachedValue = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        cachedValue = value
        CoroutineScope(Dispatchers.IO). launch {
            dataStore.edit { prefs ->
                prefs[key] = value
            }
        }
    }
}