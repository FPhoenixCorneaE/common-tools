package com.fphoenixcorneae.common.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias preferences = DataStoreOwner

/**
 * 定义接口，方便Kotlin委托实现
 */
interface DataStoreOwner {

    val dataStore: DataStore<Preferences>

    fun intPreference(default: Int? = null) =
        PreferenceProperty(::intPreferencesKey, default)

    fun doublePreference(default: Double? = null) =
        PreferenceProperty(::doublePreferencesKey, default)

    fun longPreference(default: Long? = null) =
        PreferenceProperty(::longPreferencesKey, default)

    fun floatPreference(default: Float? = null) =
        PreferenceProperty(::floatPreferencesKey, default)

    fun booleanPreference(default: Boolean? = null) =
        PreferenceProperty(::booleanPreferencesKey, default)

    fun stringPreference(default: String? = null) =
        PreferenceProperty(::stringPreferencesKey, default)

    fun stringSetPreference(default: Set<String>? = null) =
        PreferenceProperty(::stringSetPreferencesKey, default)

    fun byteArrayPreference(default: ByteArray? = null) =
        PreferenceProperty(::byteArrayPreferencesKey, default)

    /**
     * 自定义类，封装 K-V 对象，内部使用 DataStorePreference 具体操作 K-V 的存取
     */
    class PreferenceProperty<V>(
        private val key: (String) -> Preferences.Key<V>,
        private val default: V? = null,
    ) : ReadOnlyProperty<DataStoreOwner, DataStorePreference<V>> {

        /** 做缓存，如果已经取出了，不需要重复取 */
        private var cache: DataStorePreference<V>? = null

        override fun getValue(
            thisRef: DataStoreOwner,
            property: KProperty<*>
        ): DataStorePreference<V> =
            cache ?: DataStorePreference(
                thisRef.dataStore,
                key(property.name),
                default
            ).also { cache = it }
    }
}
