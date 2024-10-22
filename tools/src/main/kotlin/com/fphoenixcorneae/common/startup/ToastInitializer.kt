package com.fphoenixcorneae.common.startup

import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.applicationContext
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.util.toast.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ToastInitializer : Initializer<Unit>, CoroutineScope by MainScope() {
    override fun create(context: Context) {
        launch {
            // 初始化 ToastUtil
            ToastUtil.init(applicationContext)
            "ToastInitializer 初始化, threadId: ${Thread.currentThread()}".logd("startup")
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(ApplicationInitializer::class.java)
    }
}