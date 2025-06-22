package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kivpson.extensions.kivstore.KivStoreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun KivStoreModel.intType(default: Int = 0) = IntType(default)

class IntType(default: Int) : AbstractDataStoreType<Int>(default) {

    override fun getFromStore(): Int {
        val key = intPreferencesKey(keyName)
        return runBlocking {
            ownerSafe.dataStore.data.first()[key] ?: default
        }
    }

    override fun setToStore(value: Int) {
        val key = intPreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { prefs ->
                prefs[key] = value
            }
        }
    }
}