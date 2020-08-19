/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/18 22:46:36
 *
 * kar-framework/kar-framework.kotlin.main/commands.kt
 */

@file:JvmName("KotlinDSLCommon")
@file:JvmMultifileClass
@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package io.github.karlatemp.karframework

import io.github.karlatemp.karframework.command.*
import io.github.karlatemp.karframework.format.Translator
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Unmodifiable
import java.io.PrintStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.internal.InlineOnly


@DslMarker
annotation class CommandDsl

@CommandDsl
class CommandTreeBuilder<T>(
        val framework: ICommandFramework<T>,
        val command: ICommandNode<T>
) {

    @CommandDsl
    @InlineOnly
    inline infix fun String.command(executor: ICommandExecutor<T>) {
        command(name = this, executor = executor)
    }

    @CommandDsl
    @InlineOnly
    inline fun command(
            name: String,
            permission: String? = null,
            description: String? = null,
            tabCompiler: ITabCompiler<T>? = null,
            executor: ICommandExecutor<T>
    ) {
        command.registerSubCommand(framework.newSingleCommand().apply {
            setExecutor(executor)
                    .setName(name)
                    .setPermission(permission)
                    .setDescription(description)
                    .setTabCompiler(tabCompiler)
        }.build())
    }

    @CommandDsl
    @InlineOnly
    inline infix fun String.tree(builder: CommandTreeBuilder<T>.() -> Unit) {
        tree(name = this, builder = builder)
    }

    @CommandDsl
    @InlineOnly
    inline fun tree(
            name: String,
            permission: String? = null,
            description: String? = null,
            openDistance: Boolean = true,
            builder: CommandTreeBuilder<T>.() -> Unit
    ) {
        command.registerSubCommand(framework.newCommandTree().name(name).description(description)
                .permission(permission).openDistances(openDistance).build().apply {
                    builder(CommandTreeBuilder(framework, this))
                })
    }
}

@CommandDsl
@InlineOnly
inline infix fun <T> ICommandFramework<T>.newCommand(
        builder: CommandTreeBuilder<T>.() -> Unit
): CommandTree<T> = newCommand(
        permission = null,
        description = null,
        openDistance = false,
        builder = builder
)

@CommandDsl
@InlineOnly
inline fun <T> ICommandFramework<T>.newCommand(
        permission: String? = null,
        description: String? = null,
        openDistance: Boolean = true,
        builder: CommandTreeBuilder<T>.() -> Unit
): CommandTree<T> = CommandTree(this, "ROOT", description, permission, HashMap(), HashMap(), openDistance).also {
    builder(CommandTreeBuilder(this, it))
}

private fun `example of commands`() {
    val framework = object : AbstractCommandFramework<PrintStream>(Translator { null }) {
        override fun sendMessage(target: PrintStream, message: String) {
            TODO("Not yet implemented")
        }

        override fun hasPermission(target: PrintStream, permission: String?): Boolean {
            TODO("Not yet implemented")
        }

        override fun getName(target: PrintStream): String {
            TODO("Not yet implemented")
        }

    }

    framework newCommand {
        "hello" command { sender: @NotNull PrintStream, linkedList: @NotNull LinkedList<@NotNull String>, mutableList: @Unmodifiable @NotNull MutableList<@NotNull String> ->
            sender.println("FAQ")
        }
        "tree" tree {
        }
    }
}
