package com.kivpson.extensions.kivstore.flow_support

import com.kivpson.extensions.kivstore.KivStoreModel
import com.kivpson.extensions.kivstore.types.AbstractDataStoreType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.isAccessible

fun <T : Any> KivStoreModel.asFlow(property: KMutableProperty0<T>): Flow<T> = flow {
    property.isAccessible = true

    val delegate = property.getDelegate().let {
        require(it is AbstractDataStoreType<*>) {
            "Property ${property.name} is not backed by a KivStore type"
        }
        @Suppress("UNCHECKED_CAST")
        it as AbstractDataStoreType<T>
    }

    emit(delegate.readValue())

    dataStore.data
        .map { delegate.readValue() }
        .distinctUntilChanged()
        .collect { emit(it) }
}.buffer(Channel.BUFFERED)