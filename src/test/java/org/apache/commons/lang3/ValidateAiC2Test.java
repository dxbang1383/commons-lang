/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * C2 (Branch Coverage) test suite for {@link Validate}.
 */
public class ValidateAiC2Test {

    @Test
    public void testConstructor() {
        assertNotNull(new Validate());
    }

    // --- exclusiveBetween(double, double, double) ---
    @Test
    public void testExclusiveBetweenDouble() {
        // True branch 1: value <= start (equal or less)
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 5.0, 1.0));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 5.0, 0.5));

        // True branch 2: value >= end (equal or greater)
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 5.0, 5.0));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 5.0, 5.5));

        // True branch 3: Double.isNaN(value)
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 5.0, Double.NaN));

        // False branch: within range
        Validate.exclusiveBetween(1.0, 5.0, 2.5);
    }

    // --- exclusiveBetween(double, double, double, String) ---
    @Test
    public void testExclusiveBetweenDoubleWithMessage() {
        final String msg = "Custom msg";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.exclusiveBetween(1.0, 5.0, 1.0, msg));
        assertEquals(msg, ex.getMessage());

        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 5.0, 5.0, msg));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(1.0, 5.0, Double.NaN, msg));

        // False branch
        Validate.exclusiveBetween(1.0, 5.0, 3.0, msg);
    }

    // --- exclusiveBetween(long, long, long) ---
    @Test
    public void testExclusiveBetweenLong() {
        // True branch 1: value <= start
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(10L, 20L, 10L));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(10L, 20L, 9L));

        // True branch 2: value >= end
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(10L, 20L, 20L));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(10L, 20L, 21L));

        // False branch
        Validate.exclusiveBetween(10L, 20L, 15L);
    }

    // --- exclusiveBetween(long, long, long, String) ---
    @Test
    public void testExclusiveBetweenLongWithMessage() {
        final String msg = "Long msg";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.exclusiveBetween(10L, 20L, 10L, msg));
        assertEquals(msg, ex.getMessage());

        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween(10L, 20L, 20L, msg));

        // False branch
        Validate.exclusiveBetween(10L, 20L, 15L, msg);
    }

    // --- exclusiveBetween(T, T, Comparable<T>) ---
    @Test
    public void testExclusiveBetweenComparable() {
        // True branch: value.compareTo(start) <= 0
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween("b", "d", "a"));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween("b", "d", "b"));

        // True branch: value.compareTo(end) >= 0
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween("b", "d", "d"));
        assertThrows(IllegalArgumentException.class, () -> Validate.exclusiveBetween("b", "d", "e"));

        // False branch: inside range
        Validate.exclusiveBetween("b", "d", "c");
    }

    // --- exclusiveBetween(T, T, Comparable<T>, String, Object...) ---
    @Test
    public void testExclusiveBetweenComparableWithArgs() {
        // With varargs format
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.exclusiveBetween("b", "d", "a", "Value %s not in range", "a"));
        assertEquals("Value a not in range", ex.getMessage());

        assertThrows(IllegalArgumentException.class,
                () -> Validate.exclusiveBetween("b", "d", "e", "Value %s not in range", "e"));

        // With empty varargs (covers ArrayUtils.isEmpty(values) == true in getMessage)
        ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.exclusiveBetween("b", "d", "a", "Raw message"));
        assertEquals("Raw message", ex.getMessage());

        // False branch
        Validate.exclusiveBetween("b", "d", "c", "msg %s", "c");
    }

    // --- finite(double) ---
    @Test
    public void testFiniteDouble() {
        assertThrows(IllegalArgumentException.class, () -> Validate.finite(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> Validate.finite(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> Validate.finite(Double.NEGATIVE_INFINITY));

        // False branch
        Validate.finite(3.14);
    }

    // --- finite(double, String, Object...) ---
    @Test
    public void testFiniteDoubleWithArgs() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.finite(Double.NaN, "Not finite: %s", "NaN"));
        assertEquals("Not finite: NaN", ex.getMessage());

        ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.finite(Double.POSITIVE_INFINITY, "Not finite"));
        assertEquals("Not finite", ex.getMessage());

        assertThrows(IllegalArgumentException.class,
                () -> Validate.finite(Double.NEGATIVE_INFINITY, "Not finite"));

        // False branch
        Validate.finite(100.0, "Not finite: %s", 100.0);
    }

    // --- inclusiveBetween(double, double, double) ---
    @Test
    public void testInclusiveBetweenDouble() {
        // True branch 1: value < start
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1.0, 5.0, 0.9));

        // True branch 2: value > end
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1.0, 5.0, 5.1));

        // True branch 3: Double.isNaN(value)
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1.0, 5.0, Double.NaN));

        // False branches: at bounds & inside
        Validate.inclusiveBetween(1.0, 5.0, 1.0);
        Validate.inclusiveBetween(1.0, 5.0, 5.0);
        Validate.inclusiveBetween(1.0, 5.0, 3.0);
    }

    // --- inclusiveBetween(double, double, double, String) ---
    @Test
    public void testInclusiveBetweenDoubleWithMessage() {
        final String msg = "Inclusive double msg";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.inclusiveBetween(1.0, 5.0, 0.9, msg));
        assertEquals(msg, ex.getMessage());

        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1.0, 5.0, 5.1, msg));
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(1.0, 5.0, Double.NaN, msg));

        // False branches
        Validate.inclusiveBetween(1.0, 5.0, 1.0, msg);
        Validate.inclusiveBetween(1.0, 5.0, 5.0, msg);
        Validate.inclusiveBetween(1.0, 5.0, 3.0, msg);
    }

    // --- inclusiveBetween(long, long, long) ---
    @Test
    public void testInclusiveBetweenLong() {
        // True branch 1: value < start
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(10L, 20L, 9L));

        // True branch 2: value > end
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(10L, 20L, 21L));

        // False branches
        Validate.inclusiveBetween(10L, 20L, 10L);
        Validate.inclusiveBetween(10L, 20L, 20L);
        Validate.inclusiveBetween(10L, 20L, 15L);
    }

    // --- inclusiveBetween(long, long, long, String) ---
    @Test
    public void testInclusiveBetweenLongWithMessage() {
        final String msg = "Inclusive long msg";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.inclusiveBetween(10L, 20L, 9L, msg));
        assertEquals(msg, ex.getMessage());

        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween(10L, 20L, 21L, msg));

        // False branches
        Validate.inclusiveBetween(10L, 20L, 10L, msg);
        Validate.inclusiveBetween(10L, 20L, 20L, msg);
        Validate.inclusiveBetween(10L, 20L, 15L, msg);
    }

    // --- inclusiveBetween(T, T, Comparable<T>) ---
    @Test
    public void testInclusiveBetweenComparable() {
        // True branch: value.compareTo(start) < 0
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween("b", "d", "a"));

        // True branch: value.compareTo(end) > 0
        assertThrows(IllegalArgumentException.class, () -> Validate.inclusiveBetween("b", "d", "e"));

        // False branches
        Validate.inclusiveBetween("b", "d", "b");
        Validate.inclusiveBetween("b", "d", "d");
        Validate.inclusiveBetween("b", "d", "c");
    }

    // --- inclusiveBetween(T, T, Comparable<T>, String, Object...) ---
    @Test
    public void testInclusiveBetweenComparableWithArgs() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.inclusiveBetween("b", "d", "a", "Out: %s", "a"));
        assertEquals("Out: a", ex.getMessage());

        assertThrows(IllegalArgumentException.class,
                () -> Validate.inclusiveBetween("b", "d", "e", "Out: %s", "e"));

        // False branches
        Validate.inclusiveBetween("b", "d", "b", "msg");
        Validate.inclusiveBetween("b", "d", "d", "msg");
        Validate.inclusiveBetween("b", "d", "c", "msg");
    }

    // --- isAssignableFrom(Class<?>, Class<?>) ---
    @Test
    public void testIsAssignableFrom() {
        // True branch 1: type == null
        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(CharSequence.class, null));

        // True branch 2: superType == null
        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(null, String.class));

        // True branch 3: !superType.isAssignableFrom(type)
        assertThrows(IllegalArgumentException.class, () -> Validate.isAssignableFrom(String.class, CharSequence.class));

        // False branch: superType.isAssignableFrom(type) is true
        Validate.isAssignableFrom(CharSequence.class, String.class);
    }

    // --- isAssignableFrom(Class<?>, Class<?>, String, Object...) ---
    @Test
    public void testIsAssignableFromWithArgs() {
        // True branch: not assignable
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.isAssignableFrom(String.class, Integer.class, "Cannot assign %s to %s", "Int", "Str"));
        assertEquals("Cannot assign Int to Str", ex.getMessage());

        // False branch
        Validate.isAssignableFrom(Number.class, Integer.class, "msg");
    }

    // --- isInstanceOf(Class<?>, Object) ---
    @Test
    public void testIsInstanceOf() {
        // True branch 1: obj is null
        assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(String.class, null));

        // True branch 2: wrong type
        assertThrows(IllegalArgumentException.class, () -> Validate.isInstanceOf(String.class, 123));

        // False branch
        Validate.isInstanceOf(CharSequence.class, "hello");
    }

    // --- isInstanceOf(Class<?>, Object, String, Object...) ---
    @Test
    public void testIsInstanceOfWithArgs() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.isInstanceOf(String.class, null, "Not instance: %s", "null"));
        assertEquals("Not instance: null", ex.getMessage());

        assertThrows(IllegalArgumentException.class,
                () -> Validate.isInstanceOf(String.class, 123, "Not instance"));

        // False branch
        Validate.isInstanceOf(String.class, "hello", "msg");
    }

    // --- isTrue(boolean) ---
    @Test
    public void testIsTrue() {
        assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false));
        Validate.isTrue(true);
    }

    // --- isTrue(boolean, String, double) ---
    @Test
    public void testIsTrueDouble() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.isTrue(false, "Value is %f", 3.14));
        assertEquals(String.format("Value is %f", 3.14), ex.getMessage());

        Validate.isTrue(true, "Value is %f", 3.14);
    }

    // --- isTrue(boolean, String, long) ---
    @Test
    public void testIsTrueLong() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.isTrue(false, "Value is %d", 42L));
        assertEquals("Value is 42", ex.getMessage());

        Validate.isTrue(true, "Value is %d", 42L);
    }

    // --- isTrue(boolean, String, Object...) ---
    @Test
    public void testIsTrueWithArgs() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.isTrue(false, "Failed with %s and %d", "arg", 10));
        assertEquals("Failed with arg and 10", ex.getMessage());

        // Empty varargs
        ex = assertThrows(IllegalArgumentException.class, () -> Validate.isTrue(false, "Failed msg"));
        assertEquals("Failed msg", ex.getMessage());

        Validate.isTrue(true, "Failed msg");
    }

    // --- isTrue(boolean, Supplier<String>) ---
    @Test
    public void testIsTrueSupplier() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.isTrue(false, () -> "Supplied msg"));
        assertEquals("Supplied msg", ex.getMessage());

        Validate.isTrue(true, () -> "Supplied msg");
    }

    // --- matchesPattern(CharSequence, String) ---
    @Test
    public void testMatchesPattern() {
        assertThrows(IllegalArgumentException.class, () -> Validate.matchesPattern("abc", "\\d+"));
        Validate.matchesPattern("123", "\\d+");
    }

    // --- matchesPattern(CharSequence, String, String, Object...) ---
    @Test
    public void testMatchesPatternWithArgs() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.matchesPattern("abc", "\\d+", "Pattern failed for %s", "abc"));
        assertEquals("Pattern failed for abc", ex.getMessage());

        Validate.matchesPattern("123", "\\d+", "msg");
    }

    // --- noNullElements(Iterable) ---
    @Test
    public void testNoNullElementsIterable() {
        assertThrows(NullPointerException.class, () -> Validate.noNullElements((List<String>) null));

        List<String> listWithNull = Arrays.asList("a", null, "b");
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(listWithNull));

        List<String> validList = Arrays.asList("a", "b");
        assertSame(validList, Validate.noNullElements(validList));

        List<String> emptyList = Collections.emptyList();
        assertSame(emptyList, Validate.noNullElements(emptyList));
    }

    // --- noNullElements(Iterable, String, Object...) ---
    @Test
    public void testNoNullElementsIterableWithArgs() {
        assertThrows(NullPointerException.class,
                () -> Validate.noNullElements((List<String>) null, "msg"));

        List<String> listWithNull = Arrays.asList("x", null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.noNullElements(listWithNull, "Null element at %d", "prefix"));
        // Lambda in Validate uses ArrayUtils.addAll(values, ai.getAndIncrement())
        assertNotNull(ex.getMessage());

        List<String> validList = Arrays.asList("x", "y");
        assertSame(validList, Validate.noNullElements(validList, "msg"));
    }

    // --- noNullElements(T[]) ---
    @Test
    public void testNoNullElementsArray() {
        assertThrows(NullPointerException.class, () -> Validate.noNullElements((String[]) null));

        String[] arrayWithNull = new String[] { "a", null, "c" };
        assertThrows(IllegalArgumentException.class, () -> Validate.noNullElements(arrayWithNull));

        String[] validArray = new String[] { "a", "b" };
        assertSame(validArray, Validate.noNullElements(validArray));

        String[] emptyArray = new String[0];
        assertSame(emptyArray, Validate.noNullElements(emptyArray));
    }

    // --- noNullElements(T[], String, Object...) ---
    @Test
    public void testNoNullElementsArrayWithArgs() {
        assertThrows(NullPointerException.class,
                () -> Validate.noNullElements((String[]) null, "msg"));

        String[] arrayWithNull = new String[] { null };
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.noNullElements(arrayWithNull, "Array null at %s", "index"));
        assertNotNull(ex.getMessage());

        String[] validArray = new String[] { "a" };
        assertSame(validArray, Validate.noNullElements(validArray, "msg"));

        String[] emptyArray = new String[0];
        assertSame(emptyArray, Validate.noNullElements(emptyArray, "msg"));
    }

    // --- notBlank(CharSequence) ---
    @Test
    public void testNotBlank() {
        assertThrows(NullPointerException.class, () -> Validate.notBlank(null));
        assertThrows(IllegalArgumentException.class, () -> Validate.notBlank(""));
        assertThrows(IllegalArgumentException.class, () -> Validate.notBlank("   "));

        assertEquals("abc", Validate.notBlank("abc"));
    }

    // --- notBlank(CharSequence, String, Object...) ---
    @Test
    public void testNotBlankWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.notBlank(null, "Blank: %s", "null"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.notBlank("  ", "Blank: %s", "space"));
        assertEquals("Blank: space", ex.getMessage());

        assertEquals("hello", Validate.notBlank("hello", "msg"));
    }

    // --- notEmpty(Collection) ---
    @Test
    public void testNotEmptyCollection() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((List<String>) null));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new ArrayList<String>()));

        List<String> list = Collections.singletonList("a");
        assertSame(list, Validate.notEmpty(list));
    }

    // --- notEmpty(Collection, String, Object...) ---
    @Test
    public void testNotEmptyCollectionWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((List<String>) null, "msg %s", 1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.notEmpty(new ArrayList<String>(), "Collection empty: %d", 0));
        assertEquals("Collection empty: 0", ex.getMessage());

        List<String> list = Collections.singletonList("a");
        assertSame(list, Validate.notEmpty(list, "msg"));
    }

    // --- notEmpty(Map) ---
    @Test
    public void testNotEmptyMap() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((Map<String, String>) null));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new HashMap<String, String>()));

        Map<String, String> map = Collections.singletonMap("k", "v");
        assertSame(map, Validate.notEmpty(map));
    }

    // --- notEmpty(Map, String, Object...) ---
    @Test
    public void testNotEmptyMapWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((Map<String, String>) null, "msg %s", 1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.notEmpty(new HashMap<String, String>(), "Map empty: %s", "map"));
        assertEquals("Map empty: map", ex.getMessage());

        Map<String, String> map = Collections.singletonMap("k", "v");
        assertSame(map, Validate.notEmpty(map, "msg"));
    }

    // --- notEmpty(CharSequence) ---
    @Test
    public void testNotEmptyCharSequence() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((String) null));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(""));

        assertEquals(" ", Validate.notEmpty(" "));
        assertEquals("abc", Validate.notEmpty("abc"));
    }

    // --- notEmpty(CharSequence, String, Object...) ---
    @Test
    public void testNotEmptyCharSequenceWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((String) null, "msg %s", 1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.notEmpty("", "Empty str: %s", "str"));
        assertEquals("Empty str: str", ex.getMessage());

        assertEquals("a", Validate.notEmpty("a", "msg"));
    }

    // --- notEmpty(T[]) ---
    @Test
    public void testNotEmptyArray() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((String[]) null));
        assertThrows(IllegalArgumentException.class, () -> Validate.notEmpty(new String[0]));

        String[] array = new String[] { "a" };
        assertSame(array, Validate.notEmpty(array));
    }

    // --- notEmpty(T[], String, Object...) ---
    @Test
    public void testNotEmptyArrayWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.notEmpty((String[]) null, "msg %s", 1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.notEmpty(new String[0], "Array empty: %d", 0));
        assertEquals("Array empty: 0", ex.getMessage());

        String[] array = new String[] { "a" };
        assertSame(array, Validate.notEmpty(array, "msg"));
    }

    // --- notNaN(double) ---
    @Test
    public void testNotNaN() {
        assertThrows(IllegalArgumentException.class, () -> Validate.notNaN(Double.NaN));
        Validate.notNaN(1.23);
    }

    // --- notNaN(double, String, Object...) ---
    @Test
    public void testNotNaNWithArgs() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validate.notNaN(Double.NaN, "Is NaN: %s", "yes"));
        assertEquals("Is NaN: yes", ex.getMessage());

        // Without varargs
        ex = assertThrows(IllegalArgumentException.class, () -> Validate.notNaN(Double.NaN, "Raw NaN msg"));
        assertEquals("Raw NaN msg", ex.getMessage());

        Validate.notNaN(1.23, "msg");
    }

    // --- notNull(T) ---
    @Test
    public void testNotNull() {
        assertThrows(NullPointerException.class, () -> Validate.notNull(null));
        assertEquals("abc", Validate.notNull("abc"));
    }

    // --- notNull(T, String, Object...) ---
    @Test
    public void testNotNullWithArgs() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> Validate.notNull(null, "Object null: %s", "arg"));
        assertEquals("Object null: arg", ex.getMessage());

        ex = assertThrows(NullPointerException.class, () -> Validate.notNull(null, "Raw null msg"));
        assertEquals("Raw null msg", ex.getMessage());

        assertEquals("abc", Validate.notNull("abc", "msg"));
    }

    // --- validIndex(Collection, int) ---
    @Test
    public void testValidIndexCollection() {
        assertThrows(NullPointerException.class, () -> Validate.validIndex((List<String>) null, 0));

        List<String> list = Collections.singletonList("a");
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(list, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(list, 1));

        assertSame(list, Validate.validIndex(list, 0));
    }

    // --- validIndex(Collection, int, String, Object...) ---
    @Test
    public void testValidIndexCollectionWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.validIndex((List<String>) null, 0, "msg"));

        List<String> list = Collections.singletonList("a");
        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> Validate.validIndex(list, -1, "Bad index: %d", -1));
        assertEquals("Bad index: -1", ex.getMessage());

        assertThrows(IndexOutOfBoundsException.class,
                () -> Validate.validIndex(list, 1, "Bad index"));

        assertSame(list, Validate.validIndex(list, 0, "msg"));
    }

    // --- validIndex(CharSequence, int) ---
    @Test
    public void testValidIndexCharSequence() {
        assertThrows(NullPointerException.class, () -> Validate.validIndex((String) null, 0));

        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex("abc", -1));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex("abc", 3));

        assertEquals("abc", Validate.validIndex("abc", 0));
        assertEquals("abc", Validate.validIndex("abc", 2));
    }

    // --- validIndex(CharSequence, int, String, Object...) ---
    @Test
    public void testValidIndexCharSequenceWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.validIndex((String) null, 0, "msg"));

        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> Validate.validIndex("abc", -1, "Invalid idx: %d", -1));
        assertEquals("Invalid idx: -1", ex.getMessage());

        assertThrows(IndexOutOfBoundsException.class,
                () -> Validate.validIndex("abc", 3, "Invalid idx"));

        assertEquals("abc", Validate.validIndex("abc", 1, "msg"));
    }

    // --- validIndex(T[], int) ---
    @Test
    public void testValidIndexArray() {
        assertThrows(NullPointerException.class, () -> Validate.validIndex((String[]) null, 0));

        String[] array = new String[] { "a", "b" };
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(array, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> Validate.validIndex(array, 2));

        assertSame(array, Validate.validIndex(array, 0));
        assertSame(array, Validate.validIndex(array, 1));
    }

    // --- validIndex(T[], int, String, Object...) ---
    @Test
    public void testValidIndexArrayWithArgs() {
        assertThrows(NullPointerException.class, () -> Validate.validIndex((String[]) null, 0, "msg"));

        String[] array = new String[] { "a", "b" };
        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class,
                () -> Validate.validIndex(array, -1, "Bad array index: %d", -1));
        assertEquals("Bad array index: -1", ex.getMessage());

        assertThrows(IndexOutOfBoundsException.class,
                () -> Validate.validIndex(array, 2, "Bad array index"));

        assertSame(array, Validate.validIndex(array, 0, "msg"));
    }

    // --- validState(boolean) ---
    @Test
    public void testValidState() {
        assertThrows(IllegalStateException.class, () -> Validate.validState(false));
        Validate.validState(true);
    }

    // --- validState(boolean, String, Object...) ---
    @Test
    public void testValidStateWithArgs() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> Validate.validState(false, "State error: %s", "details"));
        assertEquals("State error: details", ex.getMessage());

        ex = assertThrows(IllegalStateException.class, () -> Validate.validState(false, "Raw state error"));
        assertEquals("Raw state error", ex.getMessage());

        Validate.validState(true, "msg");
    }
}
