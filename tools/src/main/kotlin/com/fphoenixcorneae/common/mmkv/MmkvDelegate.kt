package com.fphoenixcorneae.common.mmkv

import com.tencent.mmkv.MMKV
import java.text.DateFormat
import java.util.Date
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class MmkvDelegate<T>(
    val key: String,
    val mmkv: MMKV,
) : ReadWriteProperty<Any?, T>

class MmkvStringDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<String?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return mmkv.decodeString(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvIntDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<Int?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int? {
        return mmkv.decodeInt(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvLongDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<Long?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Long? {
        return mmkv.decodeLong(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvFloatDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<Float?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Float? {
        return mmkv.decodeFloat(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvDoubleDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<Double?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Double? {
        return mmkv.decodeDouble(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvBooleanDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<Boolean?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean? {
        return mmkv.decodeBool(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvByteArrayDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<ByteArray?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): ByteArray? {
        return mmkv.decodeBytes(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: ByteArray?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvStringSetDelegate(key: String, mmkv: MMKV) :
    MmkvDelegate<Set<String?>?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Set<String?>? {
        return mmkv.decodeStringSet(key)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String?>?) {
        value?.let {
            mmkv.encode(key, it)
        } ?: mmkv.remove(key)
    }
}

class MmkvDateDelegate(key: String, val format: DateFormat, mmkv: MMKV) :
    MmkvDelegate<Date?>(key, mmkv) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Date? {
        return mmkv.decodeString(key)?.let {
            runCatching {
                format.parse(it)
            }.getOrNull()
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Date?) {
        value?.let {
            mmkv.encode(key, runCatching { format.format(it) }.getOrNull())
        } ?: mmkv.remove(key)
    }
}
