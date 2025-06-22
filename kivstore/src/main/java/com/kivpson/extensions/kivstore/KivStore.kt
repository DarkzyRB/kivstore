package com.kivpson.extensions.kivstore

import android.content.Context
import com.kivpson.extensions.kivstore.providers.StaticContextProvider

object KivStore {

    /**
     * Initialize KivStore singleton object.
     *
     * @param context Application context
     */
    fun init(context: Context) {
        StaticContextProvider.setContext(context.applicationContext)
    }

    /**
     * Return true if KivStore singleton object is initialized via [init] function
     */
    val isInitialized: Boolean
        get() = StaticContextProvider.isInitialized
}