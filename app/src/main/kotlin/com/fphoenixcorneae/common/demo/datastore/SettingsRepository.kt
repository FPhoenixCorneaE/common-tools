package com.fphoenixcorneae.common.demo.datastore

import com.fphoenixcorneae.common.datastore.DataStoreDelegate
import com.fphoenixcorneae.common.datastore.preferences

class SettingsRepository : preferences by DataStoreDelegate("settings") {

    val bindQQSwitch by booleanPreference()

    val bindEmailSwitch by booleanPreference()
}