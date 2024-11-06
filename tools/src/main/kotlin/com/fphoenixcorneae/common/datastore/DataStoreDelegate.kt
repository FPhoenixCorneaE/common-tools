package com.fphoenixcorneae.common.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class DataStoreDelegate(val name: String) : DataStoreOwner {
    override val dataStore: DataStore<Preferences> by preferencesDataStoreSingleton(name)
}