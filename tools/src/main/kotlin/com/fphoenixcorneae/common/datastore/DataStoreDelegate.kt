package com.fphoenixcorneae.common.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.fphoenixcorneae.common.ext.applicationContext

class DataStoreDelegate(val name: String) : DataStoreOwner {

    override val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            applicationContext.preferencesDataStoreFile(name = name)
        }
}