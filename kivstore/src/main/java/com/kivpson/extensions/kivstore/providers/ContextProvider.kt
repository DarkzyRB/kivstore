package com.kivpson.extensions.kivstore.providers

import android.content.Context

interface ContextProvider {
    fun getApplicationContext(): Context
}