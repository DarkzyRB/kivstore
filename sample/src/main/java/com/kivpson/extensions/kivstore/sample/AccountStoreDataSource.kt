package com.kivpson.extensions.kivstore.sample

import com.kivpson.extensions.kivstore.flow_support.asFlow
import kotlinx.coroutines.flow.Flow


object AccountStoreDataSource{
    val authFlow: Flow<Boolean> = AccountStore.asFlow(AccountStore::auth)
    val connectedFlow: Flow<Boolean> = AccountStore.asFlow(AccountStore::connected)

    var auth: Boolean
        get() = AccountStore.auth
        set(value) { AccountStore.auth = value }

    var id: Int
        get() = AccountStore.id
        set(value) { AccountStore.id = value }

    var token: String
        get() = AccountStore.token
        set(value) { AccountStore.token = value }
}