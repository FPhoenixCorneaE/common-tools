package com.fphoenixcorneae.common.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.fphoenixcorneae.common.util.ContextUtil
import java.io.Serializable

val activityList: MutableList<Activity>
    get() = ContextUtil.getActivityList()

val launcherActivity: String
    get() = getLauncherActivity(appPackageName)

val topActivity: Activity?
    get() {
        if (activityList.isNotEmpty()) {
            for (i in activityList.indices.reversed()) {
                val activity = activityList[i]
                if (activity.isFinishing || activity.isDestroyed) {
                    continue
                }
                return activity
            }
        }
        val topActivityByReflect = topActivityByReflect
        topActivityByReflect?.let { setTopActivity(it) }
        return topActivityByReflect
    }

fun setTopActivity(activity: Activity) {
    if (activityList.contains(activity)) {
        if (activityList.last() != activity) {
            activityList.remove(activity)
            activityList.add(activity)
        }
    } else {
        activityList.add(activity)
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun getLauncherActivity(pkg: String): String {
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.setPackage(pkg)
    val info = appContext.packageManager.queryIntentActivities(intent, 0)
    val size = info.size
    if (size == 0) {
        return ""
    }
    for (i in 0 until size) {
        val ri = info[i]
        if (ri.activityInfo.processName == pkg) {
            return ri.activityInfo.name
        }
    }
    return info[0].activityInfo.name
}

private val topActivityByReflect: Activity?
    get() {
        try {
            @SuppressLint("PrivateApi") val activityThreadClass =
                Class.forName("android.app.ActivityThread")
            val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread").invoke(null)
            val mActivityListField = activityThreadClass.getDeclaredField("mActivityList")
            mActivityListField.isAccessible = true
            val activities = mActivityListField[currentActivityThreadMethod] as Map<*, *>
            for (activityRecord in activities.values) {
                val activityRecordClass: Class<*> = activityRecord!!::class.java
                val pausedField = activityRecordClass.getDeclaredField("paused")
                pausedField.isAccessible = true
                if (!pausedField.getBoolean(activityRecord)) {
                    val activityField = activityRecordClass.getDeclaredField("activity")
                    activityField.isAccessible = true
                    return activityField[activityRecord] as Activity
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

fun recreateTopActivity() {
    topActivity?.recreate()
}

/**
 * Get activity from context object
 * @return object of Activity or null if it is not Activity
 */
fun Context?.getActivity(): Activity? {
    if (this == null) {
        return null
    }
    if (this is Activity) {
        return this
    } else if (this is ContextWrapper) {
        return baseContext.getActivity()
    }
    return null
}

inline fun <reified T : Activity> Activity.startKtxActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    vararg values: Pair<String, Any>
) =
    startActivity(getIntent<T>(flags, extra, *values))

inline fun <reified T : Activity> Fragment.startKtxActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    vararg values: Pair<String, Any>
) =
    activity?.let {
        startActivity(activity?.getIntent<T>(flags, extra, *values))
    }

inline fun <reified T : Activity> Context.startKtxActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    vararg values: Pair<String, Any>
) =
    startActivity(getIntent<T>(flags, extra, *values))

inline fun <reified T : Activity> Activity.startKtxActivityForResult(
    requestCode: Int,
    flags: Int? = null,
    extra: Bundle? = null
) =
    startActivityForResult(getIntent<T>(flags, extra), requestCode)

inline fun <reified T : Activity> Fragment.startKtxActivityForResult(
    requestCode: Int,
    flags: Int? = null,
    extra: Bundle? = null
) =
    activity?.let {
        startActivityForResult(activity?.getIntent<T>(flags, extra), requestCode)
    }

inline fun <reified T : Context> Context.getIntent(
    flags: Int?,
    extra: Bundle?,
    vararg pairs: Pair<String, Any>
): Intent =
    Intent(this, T::class.java).apply {
        flags?.let { setFlags(flags) }
        extra?.let { putExtras(extra) }
        pairs.forEach { pair ->
            val name = pair.first
            when (val value = pair.second) {
                is Int -> putExtra(name, value)
                is Byte -> putExtra(name, value)
                is Char -> putExtra(name, value)
                is Short -> putExtra(name, value)
                is Boolean -> putExtra(name, value)
                is Long -> putExtra(name, value)
                is Float -> putExtra(name, value)
                is Double -> putExtra(name, value)
                is String -> putExtra(name, value)
                is CharSequence -> putExtra(name, value)
                is Parcelable -> putExtra(name, value)
                is Array<*> -> putExtra(name, value)
                is ArrayList<*> -> putExtra(name, value)
                is Serializable -> putExtra(name, value)
                is BooleanArray -> putExtra(name, value)
                is ByteArray -> putExtra(name, value)
                is ShortArray -> putExtra(name, value)
                is CharArray -> putExtra(name, value)
                is IntArray -> putExtra(name, value)
                is LongArray -> putExtra(name, value)
                is FloatArray -> putExtra(name, value)
                is DoubleArray -> putExtra(name, value)
                is Bundle -> putExtra(name, value)
                is Intent -> putExtra(name, value)
                else -> {
                }
            }
        }
    }
