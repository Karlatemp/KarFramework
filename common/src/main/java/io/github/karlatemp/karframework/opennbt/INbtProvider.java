/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:59:49
 *
 * kar-framework/kar-framework.common.main/INbtProvider.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface INbtProvider {
    @NotNull ITagByte newByte(byte value);

    @NotNull ITagByteArray newByteArray(@NotNull byte[] value);

    @NotNull ITagCompound newCompound();

    @NotNull ITagDouble newDouble(double value);

    @NotNull ITagFloat newFloat(float value);

    @NotNull ITagInt newInt(int value);

    @NotNull ITagIntArray newIntArray(@NotNull int[] value);

    @NotNull ITagList newList();

    @NotNull ITagLong newLong(long value);

    @NotNull ITagLongArray newLongArray(@NotNull long[] value);

    @NotNull ITagShort newShort(short value);

    @NotNull ITagString newString(@NotNull String value);
}
