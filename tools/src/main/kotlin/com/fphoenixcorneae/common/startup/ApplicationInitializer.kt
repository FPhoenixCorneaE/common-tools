package com.fphoenixcorneae.common.startup

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.fphoenixcorneae.common.ext.logd

/**
 * @desc：Startup 初始化Toast、Crash
 * @date：2022/04/28 11:20
 */
class ApplicationInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        sApplication = context.applicationContext as Application
        "ApplicationInitializer 初始化, threadId: ${Thread.currentThread()}".logd("startup")
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return mutableListOf()
    }

    companion object {
        internal lateinit var sApplication: Application
    }
}
