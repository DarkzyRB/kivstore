package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.FileNotFoundException

class DoubleType(default: Double) : AbstractDataStoreType<Double>(default) {

    override fun getFromStore(): Double {
        val key = doublePreferencesKey(keyName)
        return runBlocking {
            try {
                ensureStoreInitialized(key)
                ownerSafe.dataStore.data.first()[key] ?: default
            } catch (e: FileNotFoundException) {
                default
            }
        }
    }

    override fun setToStore(value: Double) {
        val key = doublePreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { it[key] = value }
        }
    }
}