/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 12:48:35
 *
 * kar-framework/kar-framework.common.main/Validate.java
 */
package io.github.karlatemp.karframework.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Validate {

    // isTrue
    //---------------------------------------------------------------------------------
    public static void isTrue(boolean expression, String message, Object value) {
        if (!expression) {
            throw new IllegalArgumentException(message + value);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException("The validated expression is false");
        }
    }

    // notNull
    //---------------------------------------------------------------------------------

    public static void notNull(Object object) {
        notNull(object, "The validated object is null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    // notEmpty array
    //---------------------------------------------------------------------------------

    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array) {
        notEmpty(array, "The validated array is empty");
    }

    // notEmpty collection
    //---------------------------------------------------------------------------------

    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.size() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "The validated collection is empty");
    }

    // notEmpty map
    //---------------------------------------------------------------------------------

    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.size() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "The validated map is empty");
    }

    // notEmpty string
    //---------------------------------------------------------------------------------

    public static void notEmpty(String string, String message) {
        if (string == null || string.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String string) {
        notEmpty(string, "The validated string is empty");
    }

    // notNullElements array
    //---------------------------------------------------------------------------------

    public static void noNullElements(Object[] array, String message) {
        Validate.notNull(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static void noNullElements(Object[] array) {
        Validate.notNull(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException("The validated array contains null element at index: " + i);
            }
        }
    }

    // notNullElements collection
    //---------------------------------------------------------------------------------

    public static void noNullElements(Collection<?> collection, String message) {
        Validate.notNull(collection);
        for (Iterator<?> it = collection.iterator(); it.hasNext(); ) {
            if (it.next() == null) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static void noNullElements(Collection<?> collection) {
        Validate.notNull(collection);
        int i = 0;
        for (Iterator<?> it = collection.iterator(); it.hasNext(); i++) {
            if (it.next() == null) {
                throw new IllegalArgumentException("The validated collection contains null element at index: " + i);
            }
        }
    }

    public static void allElementsOfType(Collection<?> collection, Class<?> clazz, String message) {
        Validate.notNull(collection);
        Validate.notNull(clazz);
        for (Iterator<?> it = collection.iterator(); it.hasNext(); ) {
            if (!clazz.isInstance(it.next())) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static void allElementsOfType(Collection<?> collection, Class<?> clazz) {
        Validate.notNull(collection);
        Validate.notNull(clazz);
        int i = 0;
        for (Iterator<?> it = collection.iterator(); it.hasNext(); i++) {
            if (!clazz.isInstance(it.next())) {
                throw new IllegalArgumentException("The validated collection contains an element not of type "
                        + clazz.getName() + " at index: " + i);
            }
        }
    }

}
