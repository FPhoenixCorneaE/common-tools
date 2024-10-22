package com.fphoenixcorneae.common.demo.mmkv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fphoenixcorneae.common.mmkv.boolean
import com.fphoenixcorneae.common.mmkv.double
import com.fphoenixcorneae.common.mmkv.float
import com.fphoenixcorneae.common.mmkv.int
import com.fphoenixcorneae.common.mmkv.long
import com.fphoenixcorneae.common.mmkv.string
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MmkvViewModel : ViewModel() {

    var stringDelegate by string("string")
    val stringResult = MutableStateFlow(stringDelegate)
    val stringFlow = MutableStateFlow(stringDelegate)

    var intDelegate by int("int")
    val intResult = MutableStateFlow(intDelegate.toString())
    val intFlow = MutableStateFlow(intDelegate.toString())

    var longDelegate by long("long")
    val longResult = MutableStateFlow(longDelegate.toString())
    val longFlow = MutableStateFlow(longDelegate.toString())

    var floatDelegate by float("float")
    val floatResult = MutableStateFlow(floatDelegate.toString())
    val floatFlow = MutableStateFlow(floatDelegate.toString())

    var doubleDelegate by double("double")
    val doubleResult = MutableStateFlow(doubleDelegate.toString())
    val doubleFlow = MutableStateFlow(doubleDelegate.toString())

    var booleanDelegate by boolean("boolean")
    val booleanResult = MutableStateFlow(booleanDelegate.toString())
    val booleanFlow = MutableStateFlow(if (booleanDelegate == true) "1" else "0")

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
    }
}