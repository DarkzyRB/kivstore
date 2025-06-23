package com.kivpson.extensions.kivstore.supports

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.kivpson.extensions.kivstore.KivStoreModel
import kotlin.reflect.KMutableProperty0

fun <T : Any> KivStoreModel.asLiveData(
    property: KMutableProperty0<T>
): LiveData<T> = asFlow(property).asLiveData()