/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 01:25:54
 *
 * kar-framework/kar-framework.common.main/Warning.java
 */

package io.github.karlatemp.karframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PACKAGE,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
public @interface Warning {
    String[] value();
}
