package com.fphoenixcorneae.common.ext.gson

import com.fphoenixcorneae.common.ext.loge
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.ref.SoftReference
import java.lang.reflect.Type

var gsonReference = SoftReference(Gson())
var gsonBuilderReference = SoftReference(GsonBuilder())

val defaultGsonBuilder: GsonBuilder
    get() = gsonBuilderReference.get() ?: GsonBuilder().also {
        gsonBuilderReference = SoftReference(it)
    }

val disableHtmlEscapingGsonBuilder = defaultGsonBuilder.disableHtmlEscaping()

val defaultGson: Gson
    get() = gsonReference.get() ?: Gson().also {
        gsonReference = SoftReference(it)
    }

val serializeNullsGson = defaultGsonBuilder.serializeNulls().create()

val disableHtmlEscapingGson = defaultGsonBuilder.disableHtmlEscaping().create()

val prettyPrintingGson = disableHtmlEscapingGsonBuilder.setPrettyPrinting().create()

val defaultDateFormatGson = defaultGsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").create()

val defaultNumberStrategyGson =
    defaultGsonBuilder.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create()

val defaultTypeAdapterGson: Gson = createGsonBuilder(true).create()
val defaultTypeAdapterGsonIgnoreNull: Gson = createGsonBuilder(false).create()

/**
 * Serializes an object into json.
 *
 * @param includeNulls Determines if nulls will be included.
 * @return object serialized into json.
 */
@JvmOverloads
fun Any?.toJson(
    includeNulls: Boolean = true
): String {
    return when {
        includeNulls -> defaultTypeAdapterGson.toJson(this)
        else -> defaultTypeAdapterGsonIgnoreNull.toJson(this)
    }
}

/**
 * Serializes an object into json.
 *
 * @param typeOfSrc    The specific genericized type of src.
 * @param includeNulls Determines if nulls will be included.
 * @return object serialized into json.
 */
@JvmOverloads
fun Any?.toJson(
    typeOfSrc: Type,
    includeNulls: Boolean = true
): String {
    return when {
        includeNulls -> defaultTypeAdapterGson.toJson(this, typeOfSrc)
        else -> defaultTypeAdapterGsonIgnoreNull.toJson(this, typeOfSrc)
    }
}

/**
 * Converts json [String] to given type.
 *
 * @param type Type json will be converted to.
 * @return instance of type
 */
fun <T> String?.toObject(type: Class<T>): T? {
    return try {
        defaultTypeAdapterGson.fromJson(this, type)
    } catch (e: JsonSyntaxException) {
        e.toString().loge()
        null
    }
}

/**
 * Converts [String] to given type.
 *
 * @param type type type json will be converted to.
 * @return instance of type
 */
fun <T> String?.toObject(type: Type): T? {
    return try {
        defaultTypeAdapterGson.fromJson(this, type)
    } catch (e: JsonSyntaxException) {
        e.toString().loge()
        null
    }
}

/**
 * Converts [Reader] to given type.
 *
 * @param type   type type json will be converted to.
 * @return instance of type
 */
fun <T> Reader.toObject(type: Class<T>): T? {
    return try {
        defaultTypeAdapterGson.fromJson(this, type)
    } catch (e: JsonSyntaxException) {
        e.toString().loge()
        null
    }
}

/**
 * Converts [Reader] to given type.
 *
 * @param type   type type json will be converted to.
 * @return instance of type
 */
fun <T> Reader.toObject(type: Type): T? {
    return try {
        defaultTypeAdapterGson.fromJson(this, type)
    } catch (e: JsonSyntaxException) {
        e.toString().loge()
        null
    }
}

/**
 * Return the type of [List] with the `type`.
 *
 * @param type The type.
 * @return the type of [List] with the `type`
 */
fun getListType(type: Type): Type {
    return TypeToken.getParameterized(MutableList::class.java, type).type
}

/**
 * Return the type of [Set] with the `type`.
 *
 * @param type The type.
 * @return the type of [Set] with the `type`
 */
fun getSetType(type: Type): Type {
    return TypeToken.getParameterized(MutableSet::class.java, type).type
}

/**
 * Return the type of map with the `keyType` and `valueType`.
 *
 * @param keyType   The type of key.
 * @param valueType The type of value.
 * @return the type of map with the `keyType` and `valueType`
 */
fun getMapType(keyType: Type, valueType: Type): Type {
    return TypeToken.getParameterized(MutableMap::class.java, keyType, valueType).type
}

/**
 * Return the type of array with the `type`.
 *
 * @param type The type.
 * @return the type of map with the `type`
 */
fun getArrayType(type: Type): Type {
    return TypeToken.getArray(type).type
}

/**
 * Return the type of `rawType` with the `typeArguments`.
 *
 * @param rawType       The raw type.
 * @param typeArguments The type of arguments.
 * @return the type of map with the `type`
 */
fun getType(rawType: Type, vararg typeArguments: Type): Type {
    return TypeToken.getParameterized(rawType, *typeArguments).type
}

/**
 * Create a pre-configured [GsonBuilder] instance.
 *
 * @param serializeNulls determines if nulls will be serialized.
 * @return [GsonBuilder] instance.
 */
fun createGsonBuilder(serializeNulls: Boolean = true): GsonBuilder {
    val builder = GsonBuilder()
    if (serializeNulls) {
        // 如果不设置 serializeNulls,序列化时默认忽略Null
        builder.serializeNulls()
    }
    return builder
        // 使打印的 json 字符串更美观，如果不设置，打印出来的字符串不分行
        .setPrettyPrinting()
        // 自定义类型适配器
        .registerTypeAdapter(Boolean::class.java, DefaultBooleanAdapter())
        .registerTypeAdapter(Double::class.java, DefaultDoubleAdapter())
        .registerTypeAdapter(Float::class.java, DefaultFloatAdapter())
        .registerTypeAdapter(Int::class.java, DefaultIntegerAdapter())
        .registerTypeAdapter(Long::class.java, DefaultLongAdapter())
        .registerTypeAdapter(String::class.java, DefaultStringAdapter())
        .registerTypeAdapter(CharSequence::class.java, DefaultCharSequenceAdapter())
}