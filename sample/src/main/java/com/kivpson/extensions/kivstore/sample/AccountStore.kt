package com.kivpson.extensions.kivstore.sample

import com.kivpson.extensions.kivstore.KivStoreModel

object AccountStore : KivStoreModel(){
    var id by intType(0)
    var token by stringType("")
    var auth by booleanType(false)
    var connected by booleanType(false)
}