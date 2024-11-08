# common-tools

通用类库：Kotlin 扩展、Kotlin-DSL（animation、layout、代码构造Drawable）、动态权限申请、缓存（内存、磁盘、内存+磁盘）。

[![](https://jitpack.io/v/FPhoenixCorneaE/Common.svg)](https://jitpack.io/#FPhoenixCorneaE/Common)

## How to include it in your project:

**Step 1.** Add the JitPack repository to your build file

```kotlin
allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

**Step 2.** Add the dependency

```kotlin
dependencies {
    implementation("com.github.FPhoenixCorneaE:Common:$latest")
}
```

## How to use：

### 1.[Kotlin扩展属性、函数](https://github.com/FPhoenixCorneaE/Common/blob/main/READMEKT.md)

### 2.若是有自定义 Startup Initializer，需要依赖`ApplicationInitializer`

```kotlin
override fun dependencies(): MutableList<Class<out Initializer<*>>> {
    return mutableListOf(ApplicationInitializer::class.java)
}
```

* 若是想自己处理全局异常，可以移除，然后依赖自己的
```xml

<provider android:authorities="${applicationId}.androidx-startup" android:exported="false"
    android:name="androidx.startup.InitializationProvider">
    
    <meta-data android:name="com.fphoenixcorneae.common.startup.GlobalExceptionHandlerInitializer"
        android:value="androidx.startup" tools:node="remove" />
</provider>
```
* 若是想自己注册Activity生命周期回调，可以移除，然后依赖自己的
```xml

<provider android:authorities="${applicationId}.androidx-startup" android:exported="false"
    android:name="androidx.startup.InitializationProvider">

    <meta-data android:name="com.fphoenixcorneae.common.startup.ActivityLifecycleInitializer"
        android:value="androidx.startup" tools:node="remove" />
</provider>
```

### 3.[动态权限申请](https://github.com/FPhoenixCorneaE/CoroutinesPermissions)

### 4.代码构造Drawable

#### GradientDrawable:

```kotlin
gradientDrawable(this) {
    shape(Shape.RECTANGLE)
    solidColor(Color.GRAY)
//            solidColor {
//                item {
//                    color(Color.RED)
//                    state(StatePressed)
//                }
//                item {
//                    color(Color.BLUE)
//                    minusState(StatePressed)
//                }
//            }
    corner {
//                radius(20f)
        radii(
            topLeftRadius = 5f,
            topRightRadius = 10f,
            bottomLeftRadius = 15f,
            bottomRightRadius = 20f
        )
    }
    stroke {
        width(3f)
        dashWidth(8f)
        dashGap(3f)
        color {
            item {
                color(Color.BLUE)
                state(StatePressed)
            }
            item {
                color(Color.RED)
                minusState(StatePressed)
            }
        }
    }
    padding {
        setPadding(left = 8f, top = 8f, right = 8f, bottom = 8f)
    }
    size(width = 100, height = 20)
//            gradient {
//                gradientCenter(0.5f, 0.5f)
//                useLevel(false)
//                gradientType(GradientType.LINEAR_GRADIENT)
//                orientation(GradientDrawable.Orientation.LEFT_RIGHT)
//                gradientRadius(10f)
//                gradientColors(intArrayOf(Color.TRANSPARENT, Color.BLACK))
//            }
}
```

#### StateListDrawable:

```kotlin
stateListDrawable {
    item {
        drawable(ColorDrawable(Color.GRAY))
        minusState(StatePressed)
    }
    item {
        drawable(ColorDrawable(Color.GREEN))
        state(StatePressed)
    }
}
```

### 5.自定义日志打印(v2.0.5加入)

> 默认使用`android.util.Log`打印，若要使用自定义日志打印或者第三方打印库，可实现接口`com.fphoenixcorneae.common.log.Printer`，然后在自定义`Application`的`attachBaseContext(base: Context?)`方法中初始化，并调用`AndroidLog.setPrinter(printer: Printer)`。

示例：

```kotlin
package com.fphoenixcorneae.common.demo

import com.fphoenixcorneae.common.log.Printer
import com.orhanobut.logger.Logger

/**
 * @desc：CustomPrinter
 * @date：2022/5/3 11:41
 */
class CustomPrinter : Printer {
    override fun v(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).v(message, args)
    }

    override fun d(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).d(message, args)
    }

    override fun i(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).i(message, args)
    }

    override fun w(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).w(message, args)
    }

    override fun e(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).e(message, args)
    }

    override fun wtf(tag: String?, message: String, vararg args: Any?) {
        Logger.t(tag).wtf(message, args)
    }

    override fun json(tag: String?, message: String?) {
        Logger.t(tag).json(message)
    }

    override fun xml(tag: String?, message: String?) {
        Logger.t(tag).xml(message)
    }
}
```

```kotlin
override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    // 初始化日志打印配置
    initLoggerConfig()
    AndroidLog.setPrinter(CustomPrinter())
}
```

### 6.内存缓存MemoryCache、磁盘缓存DiskCache、双重缓存DoubleCache

* #### MemoryCacheManager：内存缓存管理

    - [ ] 设置自定义的默认内存缓存(可选)：

      `setDefaultMemoryCache(memoryCache: MemoryCache)`，默认有一个defaultMemoryCache

    - [ ] 将值放入内存缓存：

      `put(key: String, value: Any?)`

    - [ ] 将值从内存缓存中取出：

      `get(key: String, defaultValue: T?)`

    - [ ] 获取内存缓存计数：

      `getCacheCount()`

    - [ ] 根据key将值从内存缓存中移除：

      `remove(key: String)`

    - [ ] 清除所有内存缓存：

      `clear()`

* #### DiskCacheManager：磁盘缓存管理

    - [ ] 设置自定义的默认磁盘缓存(可选)：

      `setDefaultDiskCache(diskCache: DiskCache)`，默认有一个defaultDiskCache

    - [ ] 将值放入磁盘缓存：

      `put(key: String, value: Any?)`

    - [ ] 将值从磁盘缓存中取出：

      `get(key: String, defaultValue: T?)`
      、`getParcelable(key: String, creator: Parcelable.Creator<T>)`
      /`getParcelable(key: String, parceler: Parceler<T>)`

    - [ ] 获取磁盘缓存大小(字节数)：

      `getCacheSize()`

    - [ ] 获取磁盘缓存计数：

      `getCacheCount()`

    - [ ] 根据key将值从磁盘缓存中移除：

      `remove(key: String)`

    - [ ] 清除所有磁盘缓存：

      `clear()`

* #### DoubleCacheManager：双重(内存+磁盘)缓存管理

    - [ ] 设置自定义的默认双重缓存(可选)：

      setDefaultDoubleCache(doubleCache: DoubleCache)，默认有一个defaultDoubleCache

    - [ ] 将值放入双重缓存：

      `put(key: String, value: Any?)`

    - [ ] 将值从双重缓存中取出：

      `get(key: String, defaultValue: T?)`
      、`getParcelable(key: String, creator: Parcelable.Creator<T>)`
      /`getParcelable(key: String, parceler: Parceler<T>)`

    - [ ] 获取磁盘缓存大小(字节数)：

      `getCacheDiskSize()`

    - [ ] 获取磁盘缓存计数：

      `getCacheDiskCount()`

    - [ ] 获取内存缓存计数：

      `getCacheMemoryCount()`

    - [ ] 根据key将值从双重缓存中移除：

      `remove(key: String)`

    - [ ] 清除所有双重缓存：

      `clear()`

