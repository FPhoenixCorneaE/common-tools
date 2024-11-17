package com.fphoenixcorneae.common

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.fphoenixcorneae.common.ext.appPackageName
import com.fphoenixcorneae.common.ext.isAppInstalled
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppUnitTest {

    @Test
    fun isAppInstalled() {
        println("//==============================isAppInstalled=======================================//")
        println("isAppInstalled: $appPackageName: " + isAppInstalled(appPackageName))
        println("isAppInstalled: com.fphoenixcorneae.eyepetizer: " + isAppInstalled("com.fphoenixcorneae.eyepetizer"))
        println("isAppInstalled: com.111: " + isAppInstalled("com.111"))
        println("//==============================isAppInstalled=======================================//")
    }
}