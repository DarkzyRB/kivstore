package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kivpson.extensions.kivstore.KivStoreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun KivStoreModel.doubleType(default: Double = 0.0) = DoubleType(default)

class DoubleType(default: Double) : AbstractDataStoreType<Double>(default) {

    override fun getFromStore(): Double {
        val key = stringPreferencesKey(keyName)
        return runBlocking {
            val stored = ownerSafe.dataStore.data.first()[key]
            stored?.toDoubleOrNull() ?: default
        }
    }

    override fun setToStore(value: Double) {
        val key = stringPreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { prefs ->
                prefs[key] = value.toString()
            }
        }
    }
}