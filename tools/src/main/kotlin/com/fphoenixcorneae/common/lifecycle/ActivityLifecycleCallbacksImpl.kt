package com.fphoenixcorneae.common.lifecycle

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.fphoenixcorneae.common.ext.activityList
import com.fphoenixcorneae.common.ext.fixSoftInputLeaks
import com.fphoenixcorneae.common.ext.logd
import com.fphoenixcorneae.common.ext.logi
import com.fphoenixcorneae.common.ext.runOnUiThreadDelayed
import com.fphoenixcorneae.common.ext.setTopActivity
import com.fphoenixcorneae.common.util.LanguageUtil
import java.util.concurrent.CopyOnWriteArrayList

/**
 * ActivityLifecycleCallbacks接口实现
 * 1、每一个Activity的生命周期都会回调到这里的对应方法;
 * 2、可以统计Activity的个数等;
 */
class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
    private val mStatusListeners: MutableList<OnAppStatusChangedListener?> = mutableListOf()
    private val mDestroyedListenerMap: MutableMap<Activity, MutableList<OnActivityDestroyedListener?>> =
        hashMapOf()
    private var mForegroundCount = 0
    private var mConfigCount = 0
    private var mIsBackground = false

    /**
     * onActivityCreated
     */
    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        logActivityLifecycle("onCreated(): ", activity)
        LanguageUtil.applyLanguage()
        setAnimatorsEnabled()
        setTopActivity(activity)
    }

    /**
     * onActivityStarted
     */
    override fun onActivityStarted(activity: Activity) {
        logActivityLifecycle("onStarted(): ", activity)
        if (!mIsBackground) {
            setTopActivity(activity)
        }
        if (mConfigCount < 0) {
            ++mConfigCount
        } else {
            ++mForegroundCount
        }
    }

    /**
     * onActivityResumed
     */
    override fun onActivityResumed(activity: Activity) {
        logActivityLifecycle("onResumed(): ", activity)
        setTopActivity(activity)
        if (mIsBackground) {
            mIsBackground = false
            postStatus(activity, true)
        }
        processHideSoftInputOnActivityDestroy(activity, false)
    }

    /**
     * onActivityPaused
     */
    override fun onActivityPaused(activity: Activity) {
        logActivityLifecycle("onPaused(): ", activity)
    }

    /**
     * onActivityStopped
     */
    override fun onActivityStopped(activity: Activity) {
        logActivityLifecycle("onStopped(): ", activity)
        if (activity.isChangingConfigurations) {
            --mConfigCount
        } else {
            --mForegroundCount
            if (mForegroundCount <= 0) {
                mIsBackground = true
                postStatus(activity, false)
            }
        }
        processHideSoftInputOnActivityDestroy(activity, true)
    }

    /**
     * onActivitySaveInstanceState
     */
    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
        logActivityLifecycle("onSaveInstanceState(): ", activity)
    }

    /**
     * onActivityDestroyed
     */
    override fun onActivityDestroyed(activity: Activity) {
        logActivityLifecycle("onDestroyed(): ", activity)
        activityList.remove(activity)
        consumeOnActivityDestroyedListener(activity)
        activity.fixSoftInputLeaks()
    }

    /**
     * 打印activity生命周期
     */
    private fun logActivityLifecycle(message: String, activity: Activity) {
        "ActivityLifecycle: $message${activity.componentName.className}".logd()
    }

    fun addOnAppStatusChangedListener(listener: OnAppStatusChangedListener?) {
        mStatusListeners.add(listener)
    }

    fun removeOnAppStatusChangedListener(listener: OnAppStatusChangedListener?) {
        mStatusListeners.remove(listener)
    }

    fun removeOnActivityDestroyedListener(activity: Activity?) {
        if (activity == null) return
        mDestroyedListenerMap.remove(activity)
    }

    fun addOnActivityDestroyedListener(
        activity: Activity?,
        listener: OnActivityDestroyedListener?
    ) {
        if (activity == null || listener == null) return
        var listeners: MutableList<OnActivityDestroyedListener?>? = mDestroyedListenerMap[activity]
        if (listeners == null) {
            listeners = CopyOnWriteArrayList()
            mDestroyedListenerMap[activity] = listeners
        } else {
            if (listeners.contains(listener)) return
        }
        listeners.add(listener)
    }

    /**
     * To solve close keyboard when activity onDestroy.
     * The preActivity set windowSoftInputMode will prevent
     * the keyboard from closing when curActivity onDestroy.
     */
    private fun processHideSoftInputOnActivityDestroy(
        activity: Activity,
        isSave: Boolean
    ) {
        if (isSave) {
            val attrs = activity.window.attributes
            val softInputMode = attrs.softInputMode
            activity.window.decorView.setTag(-123, softInputMode)
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        } else {
            val tag = activity.window.decorView.getTag(-123) as? Int ?: return
            runOnUiThreadDelayed(100) {
                val window = activity.window
                window?.setSoftInputMode(tag)
            }
        }
    }

    private fun postStatus(activity: Activity, isForeground: Boolean) {
        if (mStatusListeners.isEmpty()) return
        for (statusListener in mStatusListeners) {
            if (isForeground) {
                statusListener?.onForeground(activity)
            } else {
                statusListener?.onBackground(activity)
            }
        }
    }

    private fun consumeOnActivityDestroyedListener(activity: Activity) {
        val iterator: MutableIterator<Map.Entry<Activity, List<OnActivityDestroyedListener?>>> =
            mDestroyedListenerMap.entries.iterator()
        while (iterator.hasNext()) {
            val entry: Map.Entry<Activity, List<OnActivityDestroyedListener?>> =
                iterator.next()
            if (entry.key === activity) {
                val value: List<OnActivityDestroyedListener?> =
                    entry.value
                for (listener in value) {
                    listener?.onActivityDestroyed(activity)
                }
                iterator.remove()
            }
        }
    }

    /**
     * Set animators enabled.
     */
    @SuppressLint("SoonBlockedPrivateApi")
    private fun setAnimatorsEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && ValueAnimator.areAnimatorsEnabled()) {
            return
        }
        try {
            val sDurationScaleField = ValueAnimator::class.java.getDeclaredField("sDurationScale")
            sDurationScaleField.isAccessible = true
            val sDurationScale = sDurationScaleField[null] as Float
            if (sDurationScale == 0f) {
                sDurationScaleField[null] = 1f
                "setAnimatorsEnabled: Animators are enabled now!".logi()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}


interface OnAppStatusChangedListener {
    fun onForeground(activity: Activity?)
    fun onBackground(activity: Activity?)
}

interface OnActivityDestroyedListener {
    fun onActivityDestroyed(activity: Activity?)
}
