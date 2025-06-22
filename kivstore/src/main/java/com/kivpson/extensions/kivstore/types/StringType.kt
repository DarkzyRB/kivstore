package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kivpson.extensions.kivstore.KivStoreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun KivStoreModel.stringType(default: String = "") = StringType(default)

class StringType(default: String) : AbstractDataStoreType<String>(default) {

    override fun getFromStore(): String {
        val key = stringPreferencesKey(keyName)
        return runBlocking {
            ownerSafe.dataStore.data.first()[key] ?: default
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