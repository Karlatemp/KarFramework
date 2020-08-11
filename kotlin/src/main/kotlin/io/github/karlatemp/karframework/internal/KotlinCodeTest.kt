/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/11 15:24:30
 *
 * kar-framework/kar-framework.kotlin.main/KotlinCodeTest.kt
 */

package io.github.karlatemp.karframework.internal

import io.github.karlatemp.karframework.IPluginProvider
import io.github.karlatemp.karframework.registerEventLisener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.function.Consumer

object KotlinCodeTest : Consumer<IPluginProvider> {
    override fun accept(t: IPluginProvider) {
        t.registerEventLisener<PlayerJoinEvent> {
            player.sendMessage("Yes!")
        }
    }
}
