package com.kivpson.extensions.kivstore.sample

import androidx.lifecycle.LiveData
import com.kivpson.extensions.kivstore.supports.asFlow
import com.kivpson.extensions.kivstore.supports.asLiveData
import kotlinx.coroutines.flow.Flow


object AccountStoreDataSource{
    val authFlow: Flow<Boolean> = AccountStore.asFlow(AccountStore::auth)
    val authLiveData: LiveData<Boolean> = AccountStore.asLiveData(AccountStore::auth)

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