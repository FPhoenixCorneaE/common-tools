package com.fphoenixcorneae.common.demo.datastore

import com.fphoenixcorneae.common.datastore.DataStoreDelegate
import com.fphoenixcorneae.common.datastore.preferences

class UserRepository : preferences by DataStoreDelegate("user") {

    val userId by stringPreference()

    val userNickname by stringPreference()
}