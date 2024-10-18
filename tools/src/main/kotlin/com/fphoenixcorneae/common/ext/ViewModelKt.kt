package com.fphoenixcorneae.common.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

val defaultApplicationViewModelStore by lazy {
    ViewModelStore()
}

val defaultApplicationViewModelProviderFactory by lazy {
    ViewModelProvider.AndroidViewModelFactory.getInstance(applicationContext)
}

/**
 * 获取 Application 级别的 ViewModel
 */
inline fun <reified VM : ViewModel> applicationViewModel(
    noinline storeProducer: (() -> ViewModelStore)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = ViewModelLazy(
    VM::class,
    storeProducer ?: { defaultApplicationViewModelStore },
    factoryProducer ?: { defaultApplicationViewModelProviderFactory },
)