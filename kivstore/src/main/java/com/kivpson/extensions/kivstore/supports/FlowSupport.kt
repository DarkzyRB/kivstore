package com.kivpson.extensions.kivstore.supports

import com.kivpson.extensions.kivstore.KivStoreModel
import com.kivpson.extensions.kivstore.types.AbstractDataStoreType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.isAccessible

fun <T : Any> KivStoreModel.asFlow(property: KMutableProperty0<T>): Flow<T> = callbackFlow {
    property.isAccessible = true

    val delegate = property.getDelegate().let {
        require(it is AbstractDataStoreType<*>) {
            "Property ${property.name} is not backed by a KivStore type"
        }
        @Suppress("UNCHECKED_CAST")
        it as AbstractDataStoreType<T>
    }

    trySend(delegate.readValue())

    val subscription = dataStore.data
        .map { delegate.readValue() }
        .distinctUntilChanged()
        .onEach { trySend(it) }
        .launchIn(this)

    awaitClose { subscription.cancel() }
}.buffer(Channel.UNLIMITED)