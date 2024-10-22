package com.fphoenixcorneae.common.startup

import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.IoScope
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.ext.relaunchApp
import com.fphoenixcorneae.common.ext.setupGlobalExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GlobalExceptionHandlerInitializer : Initializer<Unit>, CoroutineScope by IoScope() {
    override fun create(context: Context) {
        launch {
            // 全局异常处理
            setupGlobalExceptionHandler { _, _ ->
                // 重启应用
                relaunchApp()
            }
            "GlobalExceptionHandlerInitializer 初始化, threadId: ${Thread.currentThread()}".logd("startup")
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}