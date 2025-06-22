package com.kivpson.extensions.kivstore.providers

import android.content.Context

internal object StaticContextProvider : ContextProvider {
    private var staticContext: Context? = null

    override fun getApplicationContext(): Context {
        return staticContext
            ?: throw IllegalStateException("KivStore has not been initialized.")
    }

    fun setContext(context: Context) {
        staticContext = context.applicationContext
    }

    val isInitialized
        get() = staticContext != null
}