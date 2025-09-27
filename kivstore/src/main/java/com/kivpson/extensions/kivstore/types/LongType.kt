package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.FileNotFoundException

class LongType(default: Long) : AbstractDataStoreType<Long>(default) {

    override fun getFromStore(): Long {
        val key = longPreferencesKey(keyName)
        return runBlocking {
            try {
                ensureStoreInitialized(key)
                ownerSafe.dataStore.data.first()[key] ?: default
            } catch (e: FileNotFoundException) {
                default
            }
        }
    }

    override fun setToStore(value: Long) {
        val key = longPreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { prefs ->
                prefs[key] = value
            }
        }
    }
}