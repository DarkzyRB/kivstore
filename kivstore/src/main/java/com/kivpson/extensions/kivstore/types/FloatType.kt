package com.kivpson.extensions.kivstore.types

import androidx.datastore.preferences.core.floatPreferencesKey
import com.kivpson.extensions.kivstore.KivStoreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit


fun KivStoreModel.floatPref(default: Float = 0f): AbstractDataStoreType<Float> =
    FloatType(default)


class FloatType(default: Float): AbstractDataStoreType<Float>(default) {
    override fun getFromStore(): Float {
        val key = floatPreferencesKey(keyName)
        return runBlocking {
            ownerSafe.dataStore.data.first()[key] ?: default
        }
    }

    override fun setToStore(value: Float) {
        val key = floatPreferencesKey(keyName)
        CoroutineScope(Dispatchers.IO).launch {
            ownerSafe.dataStore.edit { it[key] = value }
        }
    }
}