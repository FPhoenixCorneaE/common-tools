package com.fphoenixcorneae.common.mmkv

import com.fphoenixcorneae.common.ext.gson.toJson
import com.fphoenixcorneae.common.ext.gson.toObject
import com.fphoenixcorneae.common.ext.yyyyMMddFormat
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.text.DateFormat
import kotlin.reflect.KProperty

val defaultMmkv by lazy {
    MMKV.defaultMMKV()
}
val appMmkv by lazy {
    MMKV.mmkvWithID(
        /* mmapID = */ "app",
        /* mode = */ MMKV.SINGLE_PROCESS_MODE,
        /* cryptKey = */ "secret"
    )
}
val userMmkv by lazy {
    MMKV.mmkvWithID(
        /* mmapID = */ "user",
        /* mode = */ MMKV.SINGLE_PROCESS_MODE,
        /* cryptKey = */ "secret"
    )
}

fun string(key: String, mmkv: MMKV = defaultMmkv) = MmkvStringDelegate(key, mmkv)

fun int(key: String, mmkv: MMKV = defaultMmkv) = MmkvIntDelegate(key, mmkv)

fun long(key: String, mmkv: MMKV = defaultMmkv) = MmkvLongDelegate(key, mmkv)

fun float(key: String, mmkv: MMKV = defaultMmkv) = MmkvFloatDelegate(key, mmkv)

fun double(key: String, mmkv: MMKV = defaultMmkv) = MmkvDoubleDelegate(key, mmkv)

fun boolean(key: String, mmkv: MMKV = defaultMmkv) = MmkvBooleanDelegate(key, mmkv)

fun byteArray(key: String, mmkv: MMKV = defaultMmkv) = MmkvByteArrayDelegate(key, mmkv)

fun stringSet(key: String, mmkv: MMKV = defaultMmkv) = MmkvStringSetDelegate(key, mmkv)

fun date(key: String, format: DateFormat = yyyyMMddFormat, mmkv: MMKV = defaultMmkv) =
    MmkvDateDelegate(key, format, mmkv)

inline fun <reified T> json(key: String, mmkv: MMKV = defaultMmkv) =
    object : MmkvDelegate<T?>(key, mmkv) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return mmkv.decodeString(key)?.let {
                it.toObject(object : TypeToken<T>() {}.type)
            }
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            value?.let {
                mmkv.encode(key, it.toJson(object : TypeToken<T>() {}.type))
            } ?: mmkv.remove(key)
        }
    }
