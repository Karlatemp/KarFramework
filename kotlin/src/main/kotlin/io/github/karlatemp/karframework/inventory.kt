/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/11 14:19:45
 *
 * kar-framework/kar-framework.kotlin.main/Inventory.kt
 */
@file:JvmName("KotlinDSLBukkit")
@file:JvmMultifileClass
@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package io.github.karlatemp.karframework

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.internal.InlineOnly

@InlineOnly
inline operator fun Inventory.get(slot: Int): ItemStack? = getItem(slot)

@InlineOnly
inline operator fun Inventory.set(slot: Int, item: ItemStack?) = setItem(slot, item)

@InlineOnly
inline val ItemStack?.orAIR: ItemStack
    get() = this ?: ItemStack(Material.AIR)
