package com.kivpson.extensions.kivstore.flow_support

import com.kivpson.extensions.kivstore.KivStoreModel
import com.kivpson.extensions.kivstore.types.AbstractDataStoreType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.isAccessible

fun <T : Any> KivStoreModel.asFlow(property: KMutableProperty0<T>): Flow<T> = flow {
    property.isAccessible = true

    val delegate = property.getDelegate()
    require(delegate is AbstractDataStoreType<*>) {
        "Property ${property.name} is not backed by a KivStore type"
    }

    @Suppress("UNCHECKED_CAST")
    val typedDelegate = delegate as AbstractDataStoreType<T>

    emit(typedDelegate.readValue())

    dataStore.data
        .map { typedDelegate.readValue() }
        .distinctUntilChanged()
        .collect { emit(it) }
}