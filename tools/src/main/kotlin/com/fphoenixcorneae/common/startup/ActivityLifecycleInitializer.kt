package com.fphoenixcorneae.common.startup

import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.IoScope
import com.fphoenixcorneae.common.ext.applicationContext
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.lifecycle.ActivityLifecycleCallbacksImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ActivityLifecycleInitializer : Initializer<Unit>, CoroutineScope by IoScope() {
    override fun create(context: Context) {
        launch {
            // 注册应用生命周期回调
            applicationContext.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
            "ActivityLifecycleInitializer 初始化, threadId: ${Thread.currentThread()}".logd("startup")
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(ApplicationInitializer::class.java)
    }
}