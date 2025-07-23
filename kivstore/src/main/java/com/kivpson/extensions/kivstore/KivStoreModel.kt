package com.kivpson.extensions.kivstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kivpson.extensions.kivstore.providers.ContextProvider
import com.kivpson.extensions.kivstore.providers.KivStorePreferenceDelegate
import com.kivpson.extensions.kivstore.providers.StaticContextProvider
import com.kivpson.extensions.kivstore.types.AbstractDataStoreType
import com.kivpson.extensions.kivstore.types.BooleanType
import com.kivpson.extensions.kivstore.types.DoubleType
import com.kivpson.extensions.kivstore.types.FloatType
import com.kivpson.extensions.kivstore.types.IntType
import com.kivpson.extensions.kivstore.types.LongType
import com.kivpson.extensions.kivstore.types.StringSetType
import com.kivpson.extensions.kivstore.types.StringType
import kotlinx.coroutines.runBlocking

abstract class KivStoreModel(
    private val contextProvider: ContextProvider = StaticContextProvider
) {
    open val storeName: String
        get() = javaClass.simpleName

    private val context: Context
        get() = contextProvider.getApplicationContext()

    private val internalDataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(storeName)
        }
    }

    val dataStore: DataStore<Preferences>
        get() = internalDataStore

    fun <T> preferenceDelegate(
        key: Preferences.Key<T>,
        defaultValue: T
    ) = KivStorePreferenceDelegate(dataStore, key, defaultValue)

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    fun clearBlocking() = runBlocking {
        clear()
    }


    // Types
    fun booleanType(default: Boolean = false) = BooleanType(default)
    fun doubleType(default: Double = 0.0) = DoubleType(default)
    fun floatType(default: Float = 0f): AbstractDataStoreType<Float> =
        FloatType(default)
    fun intType(default: Int = 0) = IntType(default)
    fun longType(default: Long = 0L) = LongType(default)
    fun stringSetType(default: Set<String> = LinkedHashSet()): AbstractDataStoreType<Set<String>> =
        StringSetType(default)
    fun stringType(default: String = "") = StringType(default)
}