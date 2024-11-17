package com.fphoenixcorneae.common.ext

import java.util.regex.Pattern

/** 合法正整数正则 */
const val PATTERN_LEGAL_POSITIVE_INTEGER = "^(0|[1-9]\\d*)$"

/** 合法正小数正则 */
const val PATTERN_LEGAL_DECIMAL = "^(0|[1-9]\\d*)\\.\\d+$"

/** 手机号码正则 */
const val PATTERN_MOBILE_PHONE = "^1[1-9][0-9]{9}$"

/** 邮箱正则 */
const val PATTERN_EMAIL =
    "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"

/**
 * 判断给定字符串是否为合法正整数
 */
fun CharSequence?.isLegalPositiveInt(): Boolean =
    this?.let { Pattern.matches(PATTERN_LEGAL_POSITIVE_INTEGER, it) }.orFalse()

/**
 * 判断给定字符串是否为合法正小数
 */
fun CharSequence?.isLegalPositiveDecimal(): Boolean =
    this?.let { Pattern.matches(PATTERN_LEGAL_DECIMAL, it) }.orFalse()

/**
 * 判断当前字符集是否为一个手机号码
 */
fun CharSequence?.isPhoneNumber(): Boolean =
    this?.let { Pattern.matches(PATTERN_MOBILE_PHONE, it) }.orFalse()

/**
 * 判断当前字符集是否为一个邮箱
 */
fun CharSequence?.isEmail(): Boolean =
    this?.let { Pattern.matches(PATTERN_EMAIL, it) }.orFalse()