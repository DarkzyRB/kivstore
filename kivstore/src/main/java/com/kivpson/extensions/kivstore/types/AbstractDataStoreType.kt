package com.kivpson.extensions.kivstore.types

import com.kivpson.extensions.kivstore.KivStoreModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AbstractDataStoreType<T : Any>(
    protected val default: T
) : ReadWriteProperty<KivStoreModel, T> {

    private lateinit var property: KProperty<*>
    private lateinit var owner: KivStoreModel

    protected val keyName: String
        get() = if (::property.isInitialized) property.name
        else  throw IllegalStateException("Property not initialization")

    @Volatile
    private var cachedValue: T? = null

    protected val ownerSafe: KivStoreModel
        get() = if (::owner.isInitialized) owner
        else throw IllegalStateException("Delegate used before initialization")

    operator fun provideDelegate(
        thisRef: KivStoreModel,
        property: KProperty<*>
    ): ReadWriteProperty<KivStoreModel, T> {
        this.property = property
        this.owner = thisRef
        return this
    }

    override fun getValue(thisRef: KivStoreModel, property: KProperty<*>): T {
        return cachedValue ?: runBlocking {
            val value = getFromStore()
            cachedValue = value
            value
        }
    }

    override fun setValue(thisRef: KivStoreModel, property: KProperty<*>, value: T) {
        cachedValue = value
        CoroutineScope(Dispatchers.IO).launch {
            setToStore(value)
        }
    }

    protected abstract fun getFromStore(): T
    protected abstract fun setToStore(value: T)

    fun readValue(): T = getFromStore()
}
