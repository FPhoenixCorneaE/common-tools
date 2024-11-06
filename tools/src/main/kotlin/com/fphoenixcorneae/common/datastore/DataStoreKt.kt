package com.fphoenixcorneae.common.datastore

import android.content.Context
import androidx.annotation.GuardedBy
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.fphoenixcorneae.common.ext.applicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun preferencesDataStoreSingleton(
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) = PreferenceDataStoreSingleton.getInstance(name, corruptionHandler, produceMigrations, scope)

class PreferenceDataStoreSingleton {
    companion object {
        private val INSTANCE by lazy { linkedMapOf<String, PreferenceDataStoreSingletonDelegate>() }

        fun getInstance(
            name: String,
            corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
            produceMigrations: (Context) -> List<DataMigration<Preferences>>,
            scope: CoroutineScope
        ): PreferenceDataStoreSingletonDelegate {
            return INSTANCE[name] ?: synchronized(this) {
                INSTANCE[name] ?: PreferenceDataStoreSingletonDelegate(
                    name,
                    corruptionHandler,
                    produceMigrations,
                    scope
                ).also {
                    INSTANCE[name] = it
                }
            }
        }
    }
}

/**
 * Delegate class to manage Preferences DataStore as a singleton.
 */
class PreferenceDataStoreSingletonDelegate internal constructor(
    private val name: String,
    private val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    private val produceMigrations: (Context) -> List<DataMigration<Preferences>>,
    private val scope: CoroutineScope
) : ReadOnlyProperty<Any?, DataStore<Preferences>> {

    private val lock = Any()

    @GuardedBy("lock")
    @Volatile
    private var INSTANCE: DataStore<Preferences>? = null

    /**
     * Gets the instance of the DataStore.
     *
     * @param thisRef must be an instance of [Context]
     * @param property not used
     */
    override fun getValue(thisRef: Any?, property: KProperty<*>): DataStore<Preferences> {
        return INSTANCE ?: synchronized(lock) {
            if (INSTANCE == null) {

                INSTANCE = PreferenceDataStoreFactory.create(
                    corruptionHandler = corruptionHandler,
                    migrations = produceMigrations(applicationContext),
                    scope = scope
                ) {
                    applicationContext.preferencesDataStoreFile(name)
                }
            }
            INSTANCE!!
        }
    }
}