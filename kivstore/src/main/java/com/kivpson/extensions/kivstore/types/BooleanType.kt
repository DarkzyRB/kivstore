package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.kivpson.extensions.kivstore.KivStoreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun KivStoreModel.booleanType(default: Boolean = false) = BooleanType(default)

class BooleanType(default: Boolean) : AbstractDataStoreType<Boolean>(default) {

    override fun getFromStore(): Boolean {
        val key = booleanPreferencesKey(keyName)
        return runBlocking {
            ownerSafe.dataStore.data.first()[key] ?: default
        }
    }

    override fun setToStore(value: Boolean) {
        val key = booleanPreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { prefs ->
                prefs[key] = value
            }
        }
    }

}