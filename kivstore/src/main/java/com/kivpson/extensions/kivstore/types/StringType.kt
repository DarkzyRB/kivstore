package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.FileNotFoundException

class StringType(default: String) : AbstractDataStoreType<String>(default) {

    override fun getFromStore(): String {
        val key = stringPreferencesKey(keyName)
        return runBlocking {
            try {
                ensureStoreInitialized(key)
                ownerSafe.dataStore.data.first()[key] ?: default
            } catch (e: FileNotFoundException) {
                default
            }
        }
    }

    override fun setToStore(value: String) {
        val key = stringPreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { prefs ->
                prefs[key] = value
            }
        }
    }
}