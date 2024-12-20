package com.fphoenixcorneae.common.util.toast.style

import android.util.TypedValue
import android.view.Gravity
import com.fphoenixcorneae.common.ext.applicationContext
import com.fphoenixcorneae.common.util.toast.IToastStyle

/**
 * Toast 样式基类
 */
abstract class BaseToastStyle : IToastStyle {
    override val gravity: Int
        get() = Gravity.CENTER

    override val xOffset: Int
        get() = 0

    override val yOffset: Int
        get() = 0

    override val z: Int
        get() = 30

    override val maxLines: Int
        get() = 5

    override val paddingEnd: Int
        get() = paddingStart

    override val paddingBottom: Int
        get() = paddingTop

    /**
     * dp转px
     */
    protected fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            applicationContext.resources.displayMetrics
        ).toInt()
    }

    /**
     * sp转px
     */
    protected fun sp2px(spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            applicationContext.resources.displayMetrics
        ).toInt()
    }
}