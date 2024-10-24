package com.fphoenixcorneae.common.demo.mmkv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fphoenixcorneae.common.demo.User
import com.fphoenixcorneae.common.ext.gson.toJson
import com.fphoenixcorneae.common.mmkv.boolean
import com.fphoenixcorneae.common.mmkv.byteArray
import com.fphoenixcorneae.common.mmkv.double
import com.fphoenixcorneae.common.mmkv.float
import com.fphoenixcorneae.common.mmkv.int
import com.fphoenixcorneae.common.mmkv.json
import com.fphoenixcorneae.common.mmkv.long
import com.fphoenixcorneae.common.mmkv.string
import com.fphoenixcorneae.common.mmkv.stringSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MmkvViewModel : ViewModel() {

    private var stringDelegate by string("string")
    val stringResult = MutableStateFlow(stringDelegate)
    val stringFlow = MutableStateFlow(stringDelegate)

    private var intDelegate by int("int")
    val intResult = MutableStateFlow(intDelegate.toString())
    val intFlow = MutableStateFlow(intDelegate.toString())

    private var longDelegate by long("long")
    val longResult = MutableStateFlow(longDelegate.toString())
    val longFlow = MutableStateFlow(longDelegate.toString())

    private var floatDelegate by float("float")
    val floatResult = MutableStateFlow(floatDelegate.toString())
    val floatFlow = MutableStateFlow(floatDelegate.toString())

    private var doubleDelegate by double("double")
    val doubleResult = MutableStateFlow(doubleDelegate.toString())
    val doubleFlow = MutableStateFlow(doubleDelegate.toString())

    private var booleanDelegate by boolean("boolean")
    val booleanResult = MutableStateFlow(booleanDelegate.toString())
    val booleanFlow = MutableStateFlow(if (booleanDelegate == true) "1" else "0")

    private var byteArrayDelegate by byteArray("byteArray")
    val byteArrayResult = MutableStateFlow(String(byteArrayDelegate ?: byteArrayOf()))
    val byteArrayFlow = MutableStateFlow(String(byteArrayDelegate ?: byteArrayOf()))

    private var stringSetDelegate by stringSet("stringSet")
    val stringSetResult = MutableStateFlow(stringSetDelegate?.joinToString(" "))
    val stringSetFlow = MutableStateFlow(stringSetDelegate?.joinToString(" "))

    private var jsonDelegate by json<User>("json")
    val jsonResult = MutableStateFlow(jsonDelegate.toJson())
    val jsonFlow = MutableStateFlow(jsonDelegate?.name + " " + jsonDelegate?.age)

    init {
        viewModelScope.launch {
            stringFlow.collect {
                stringDelegate = it
                stringResult.emit(stringDelegate)
            }
        }
        viewModelScope.launch {
            intFlow.collect {
                intDelegate = it.toIntOrNull()
                intResult.emit(intDelegate.toString())
            }
        }
        viewModelScope.launch {
            longFlow.collect {
                longDelegate = it.toLongOrNull()
                longResult.emit(longDelegate.toString())
            }
        }
        viewModelScope.launch {
            floatFlow.collect {
                floatDelegate = it.toFloatOrNull()
                floatResult.emit(floatDelegate.toString())
            }
        }
        viewModelScope.launch {
            doubleFlow.collect {
                doubleDelegate = it.toDoubleOrNull()
                doubleResult.emit(doubleDelegate.toString())
            }
        }
        viewModelScope.launch {
            booleanFlow.collect {
                booleanDelegate = if (it == "1") true else false
                booleanResult.emit(booleanDelegate.toString())
            }
        }
        viewModelScope.launch {
            byteArrayFlow.collect {
                byteArrayDelegate = it.toByteArray()
                byteArrayResult.emit(String(byteArrayDelegate ?: byteArrayOf()))
            }
        }
        viewModelScope.launch {
            stringSetFlow.collect {
                stringSetDelegate = it?.split(" ")?.toSet()
                stringSetResult.emit(stringSetDelegate?.joinToString(" "))
            }
        }
        viewModelScope.launch {
            jsonFlow.collect {
                jsonDelegate =
                    User(it.split(" ").getOrNull(0), it.split(" ").getOrNull(1)?.toIntOrNull())
                jsonResult.emit(jsonDelegate.toJson())
            }
        }
    }
}