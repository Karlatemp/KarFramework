/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/11 14:46:48
 *
 * kar-framework/kar-framework.kotlin.main/events.kt
 */
@file:JvmName("KotlinDSLCommon")
@file:JvmMultifileClass

package io.github.karlatemp.karframework

import io.github.karlatemp.karframework.bukkit.BukkitPluginProvider
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.RegisteredListener

private val eventProviders = run {
    val result = ArrayList<
            (IPluginProvider, Class<*>) -> (((Any) -> Unit, Int, Boolean) -> Unit)?
            >()
    runCatching {
        val eventClass = Class.forName("org.bukkit.event.Event")
        result.add { provider, klass ->
            if (provider is BukkitPluginProvider) {
                if (eventClass.isAssignableFrom(klass)) {
                    val handlers = runCatching {
                        klass.getMethod("getHandlerList").apply {
                            isAccessible = true
                        }.invoke(null) as HandlerList
                    }.getOrElse { exception ->
                        throw RuntimeException("Exception in find event handler", exception)
                    }
                    val pl = provider.plugin
                    return@add object : ((Any) -> Unit, Int, Boolean) -> Unit, Listener {
                        override fun invoke(p1: (Any) -> Unit, p2: Int, p3: Boolean) {
                            val registered = RegisteredListener(
                                    this, { _, event ->
                                if (klass.isInstance(event)) {
                                    p1(event)
                                }
                            }, EventPriority.values()[p2], pl, p3)
                            handlers.register(registered)
                        }
                    }
                }
            }
            return@add null
        }
    }
    result
}


inline fun <reified T : Any> IPluginProvider.registerEventLisener(
        eventPriority: Int = 2,
        ignoreCancelled: Boolean = false,
        noinline executor: T.() -> Unit
) {
    registerEventListener(T::class.java, eventPriority, ignoreCancelled, executor)
}

@PublishedApi
internal fun <T> IPluginProvider.registerEventListener(
        klass: Class<T>,
        eventPriority: Int,
        ignoreCancelled: Boolean,
        executor: T.() -> Unit
) {
    eventProviders.forEach { factory ->
        val provider = factory.invoke(this, klass) ?: return@forEach
        @Suppress("UNCHECKED_CAST")
        provider(executor as (Any) -> Unit, eventPriority, ignoreCancelled)
        return
    }
    error("No any provider match.")
}