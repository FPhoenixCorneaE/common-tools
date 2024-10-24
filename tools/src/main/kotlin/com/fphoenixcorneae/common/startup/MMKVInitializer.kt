package com.fphoenixcorneae.common.startup

import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.IoScope
import com.fphoenixcorneae.common.ext.logd
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MMKVInitializer : Initializer<Unit>, CoroutineScope by IoScope() {
    override fun create(context: Context) {
        launch {
            MMKV.initialize(context)
            "MMKVInitializer 初始化, thread: ${Thread.currentThread()}".logd("startup")
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(ApplicationInitializer::class.java)
    }
}