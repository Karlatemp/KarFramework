/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/11 14:32:15
 *
 * kar-framework/kar-framework.kotlin.main/atomic.kt
 */
@file:JvmName("KotlinDSLCommon")
@file:JvmMultifileClass

package io.github.karlatemp.karframework

import com.google.common.util.concurrent.AtomicDouble
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

operator fun <T> AtomicReference<T>.getValue(`this`: Any?, property: Any?): T = get()
operator fun <T> AtomicReference<T>.setValue(`this`: Any?, property: Any?, value: T) = set(value)

operator fun AtomicBoolean.getValue(`this`: Any?, property: Any?) = get()
operator fun AtomicBoolean.setValue(`this`: Any?, property: Any?, value: Boolean) = set(value)

operator fun AtomicInteger.getValue(`this`: Any, property: Any?) = get()
operator fun AtomicInteger.setValue(`this`: Any, property: Any?, value: Int) = set(value)

operator fun AtomicLong.getValue(`this`: Any, property: Any?) = get()
operator fun AtomicLong.setValue(`this`: Any, property: Any?, value: Long) = set(value)

operator fun AtomicDouble.getValue(`this`: Any, property: Any?) = get()
operator fun AtomicDouble.setValue(`this`: Any, property: Any?, value: Double) = set(value)

operator fun <T> Array<T>.getValue(`this`: Any?, property: Any?): T = get(0)
operator fun <T> Array<T>.setValue(`this`: Any?, property: Any?, value: T) = set(0, value)


