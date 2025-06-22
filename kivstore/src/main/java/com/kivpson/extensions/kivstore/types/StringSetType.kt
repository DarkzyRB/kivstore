package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.kivpson.extensions.kivstore.KivStoreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun KivStoreModel.stringSetPref(default: Set<String> = LinkedHashSet()): AbstractDataStoreType<Set<String>> =
    StringSetType(default)


class StringSetType(default: Set<String>): AbstractDataStoreType<Set<String>>(default) {
    override fun getFromStore(): Set<String> {
        val key = stringSetPreferencesKey(keyName)
        return runBlocking {
            ownerSafe.dataStore.data.first()[key] ?: default
        }
    }

    override fun setToStore(value: Set<String>) {
        val key = stringSetPreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { it[key] = value }
        }
    }
}