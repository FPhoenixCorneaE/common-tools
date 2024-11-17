package com.fphoenixcorneae.common.ext

import android.util.TypedValue


/**
 * Int 类型： dp 转换为 px
 */
val Int.dp: Int get() = Dp.toInt()
val Int.Dp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            appResources.displayMetrics
        )

/**
 * Float 类型： dp 转换为 px
 */
val Float.dp: Int get() = Dp.toInt()
val Float.Dp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            appResources.displayMetrics
        )

/**
 * Double 类型： dp 转换为 px
 */
val Double.dp: Int get() = Dp.toInt()
val Double.Dp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            appResources.displayMetrics
        )

/**
 * Int 类型： sp 转换为 px
 */
val Int.sp: Int get() = Sp.toInt()
val Int.Sp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            appResources.displayMetrics
        )

/**
 * Float 类型： sp 转换为 px
 */
val Float.sp: Int get() = Sp.toInt()
val Float.Sp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            appResources.displayMetrics
        )

/**
 * Double 类型： sp 转换为 px
 */
val Double.sp: Int get() = Sp.toInt()
val Double.Sp: Float
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            appResources.displayMetrics
        )

/**
 * Int 类型： px 转换为 dp
 */
fun Int.toDp(): Float = run {
    val scale = appResources.displayMetrics.density
    this / scale + 0.5f
}

/**
 * Float 类型： px 转换为 dp
 */
fun Float.toDp(): Float = run {
    val scale = appResources.displayMetrics.density
    this / scale + 0.5f
}

/**
 * Double 类型： px 转换为 dp
 */
fun Double.toDp(): Float = run {
    val scale = appResources.displayMetrics.density
    this / scale + 0.5f
}.toFloat()

/**
 * Int 类型： px 转换为 sp
 */
fun Int.toSp(): Float = run {
    val fontScale = appResources.displayMetrics.scaledDensity
    this / fontScale + 0.5f
}

/**
 * Float 类型： px 转换为 sp
 */
fun Float.toSp(): Float = run {
    val fontScale = appResources.displayMetrics.scaledDensity
    this / fontScale + 0.5f
}

/**
 * Double 类型： px 转换为 sp
 */
fun Double.toSp(): Float = run {
    val fontScale = appResources.displayMetrics.scaledDensity
    this / fontScale + 0.5f
}.toFloat()

