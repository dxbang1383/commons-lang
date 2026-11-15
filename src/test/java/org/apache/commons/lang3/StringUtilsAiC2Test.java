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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * C2 (Branch Coverage) test suite for {@link StringUtils}.
 */
public class StringUtilsAiC2Test {

    @Test
    public void testConstructor() {
        assertNotNull(new StringUtils());
    }

    // --- Empty / Blank ---
    @Test
    public void testEmptyBlank() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("bob"));

        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("bob"));

        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank(" \t \r \n "));
        assertFalse(StringUtils.isBlank("bob"));
        assertFalse(StringUtils.isBlank(" bob "));

        assertFalse(StringUtils.isNotBlank(null));
        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertTrue(StringUtils.isNotBlank("bob"));

        // All / Any / None Empty
        assertTrue(StringUtils.isAllEmpty((CharSequence[]) null));
        assertTrue(StringUtils.isAllEmpty());
        assertTrue(StringUtils.isAllEmpty(null, ""));
        assertFalse(StringUtils.isAllEmpty(null, "a"));
        assertFalse(StringUtils.isAllEmpty("a", "b"));

        assertFalse(StringUtils.isAnyEmpty((CharSequence[]) null));
        assertFalse(StringUtils.isAnyEmpty());
        assertTrue(StringUtils.isAnyEmpty(null, "foo"));
        assertTrue(StringUtils.isAnyEmpty("", "bar"));
        assertFalse(StringUtils.isAnyEmpty("foo", "bar"));

        assertTrue(StringUtils.isNoneEmpty((CharSequence[]) null));
        assertTrue(StringUtils.isNoneEmpty());
        assertFalse(StringUtils.isNoneEmpty(null, "foo"));
        assertFalse(StringUtils.isNoneEmpty("", "bar"));
        assertTrue(StringUtils.isNoneEmpty("foo", "bar"));

        // All / Any / None Blank
        assertTrue(StringUtils.isAllBlank((CharSequence[]) null));
        assertTrue(StringUtils.isAllBlank());
        assertTrue(StringUtils.isAllBlank(null, " ", ""));
        assertFalse(StringUtils.isAllBlank(null, "a"));

        assertFalse(StringUtils.isAnyBlank((CharSequence[]) null));
        assertFalse(StringUtils.isAnyBlank());
        assertTrue(StringUtils.isAnyBlank(null, "foo"));
        assertTrue(StringUtils.isAnyBlank(" ", "bar"));
        assertFalse(StringUtils.isAnyBlank("foo", "bar"));

        assertTrue(StringUtils.isNoneBlank((CharSequence[]) null));
        assertTrue(StringUtils.isNoneBlank());
        assertFalse(StringUtils.isNoneBlank(null, "foo"));
        assertFalse(StringUtils.isNoneBlank(" ", "bar"));
        assertTrue(StringUtils.isNoneBlank("foo", "bar"));
    }

    // --- Defaults ---
    @Test
    public void testDefaultIf() {
        assertEquals("bat", StringUtils.defaultString(null, "bat"));
        assertEquals("", StringUtils.defaultString("", "bat"));
        assertEquals("bat", StringUtils.defaultString("bat", "xyz"));
        assertEquals("", StringUtils.defaultString(null));
        assertEquals("bat", StringUtils.defaultString("bat"));

        assertEquals("bat", StringUtils.defaultIfEmpty(null, "bat"));
        assertEquals("bat", StringUtils.defaultIfEmpty("", "bat"));
        assertEquals(" ", StringUtils.defaultIfEmpty(" ", "bat"));
        assertEquals("foo", StringUtils.defaultIfEmpty("foo", "bat"));

        assertEquals("bat", StringUtils.defaultIfBlank(null, "bat"));
        assertEquals("bat", StringUtils.defaultIfBlank("", "bat"));
        assertEquals("bat", StringUtils.defaultIfBlank(" ", "bat"));
        assertEquals("foo", StringUtils.defaultIfBlank("foo", "bat"));

        assertNull(StringUtils.firstNonEmpty((String[]) null));
        assertNull(StringUtils.firstNonEmpty());
        assertNull(StringUtils.firstNonEmpty(null, ""));
        assertEquals(" ", StringUtils.firstNonEmpty(null, "", " ", "foo"));

        assertNull(StringUtils.firstNonBlank((String[]) null));
        assertNull(StringUtils.firstNonBlank());
        assertNull(StringUtils.firstNonBlank(null, "", " "));
        assertEquals("foo", StringUtils.firstNonBlank(null, "", " ", "foo"));

        assertEquals("def", StringUtils.getIfEmpty(null, () -> "def"));
        assertEquals("def", StringUtils.getIfEmpty("", () -> "def"));
        assertEquals("val", StringUtils.getIfEmpty("val", () -> "def"));
        assertEquals("val", StringUtils.getIfEmpty("val", null));

        assertEquals("def", StringUtils.getIfBlank(null, () -> "def"));
        assertEquals("def", StringUtils.getIfBlank(" ", () -> "def"));
        assertEquals("val", StringUtils.getIfBlank("val", () -> "def"));
        assertEquals("val", StringUtils.getIfBlank("val", null));
    }

    // --- Trim / Strip ---
    @Test
    public void testTrimStrip() {
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("     "));
        assertEquals("abc", StringUtils.trim("  abc  "));

        assertNull(StringUtils.trimToNull(null));
        assertNull(StringUtils.trimToNull(""));
        assertNull(StringUtils.trimToNull("     "));
        assertEquals("abc", StringUtils.trimToNull("  abc  "));

        assertEquals("", StringUtils.trimToEmpty(null));
        assertEquals("", StringUtils.trimToEmpty(""));
        assertEquals("", StringUtils.trimToEmpty("     "));
        assertEquals("abc", StringUtils.trimToEmpty("  abc  "));

        assertNull(StringUtils.trimAsciiControl(null));
        assertEquals("", StringUtils.trimAsciiControl(""));
        assertEquals(" abc ", StringUtils.trimAsciiControl("\u0001\u001f abc \u0000"));

        assertNull(StringUtils.strip(null));
        assertEquals("", StringUtils.strip(""));
        assertEquals("", StringUtils.strip("   "));
        assertEquals("abc", StringUtils.strip("  abc  "));
        assertEquals("abc", StringUtils.strip("yxabcxz", "xyz"));
        assertEquals("abc", StringUtils.strip("  abc  ", null));

        assertNull(StringUtils.stripToNull(null));
        assertNull(StringUtils.stripToNull(""));
        assertNull(StringUtils.stripToNull("   "));
        assertEquals("abc", StringUtils.stripToNull("  abc  "));

        assertEquals("", StringUtils.stripToEmpty(null));
        assertEquals("", StringUtils.stripToEmpty(""));
        assertEquals("abc", StringUtils.stripToEmpty("  abc  "));

        assertNull(StringUtils.stripStart(null, "a"));
        assertEquals("", StringUtils.stripStart("", "a"));
        assertEquals("abc  ", StringUtils.stripStart("  abc  ", null));
        assertEquals("abc", StringUtils.stripStart("yxabc", "xyz"));

        assertNull(StringUtils.stripEnd(null, "a"));
        assertEquals("", StringUtils.stripEnd("", "a"));
        assertEquals("  abc", StringUtils.stripEnd("  abc  ", null));
        assertEquals("abc", StringUtils.stripEnd("abcyx", "xyz"));

        assertNull(StringUtils.stripAll((String[]) null));
        assertArrayEquals(new String[0], StringUtils.stripAll(new String[0]));
        assertArrayEquals(new String[] {"abc", "def"}, StringUtils.stripAll("  abc  ", "  def  "));
        assertArrayEquals(new String[] {"abc", "def"}, StringUtils.stripAll(new String[] {"yxabc", "defz"}, "xyz"));

        assertNull(StringUtils.stripAccents(null));
        assertEquals("", StringUtils.stripAccents(""));
        assertEquals("control", StringUtils.stripAccents("control"));
        assertEquals("eclair", StringUtils.stripAccents("éclair"));
        assertEquals("facade", StringUtils.stripAccents("façade"));
    }

    // --- Equals / Compare ---
    @Test
    public void testEqualsCompare() {
        assertTrue(StringUtils.equals(null, null));
        assertFalse(StringUtils.equals(null, "abc"));
        assertFalse(StringUtils.equals("abc", null));
        assertTrue(StringUtils.equals("abc", "abc"));
        assertFalse(StringUtils.equals("abc", "ABC"));

        assertTrue(StringUtils.equalsIgnoreCase(null, null));
        assertFalse(StringUtils.equalsIgnoreCase(null, "abc"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", null));
        assertTrue(StringUtils.equalsIgnoreCase("abc", "ABC"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", "def"));

        assertFalse(StringUtils.equalsAny(null, (CharSequence[]) null));
        assertFalse(StringUtils.equalsAny("abc"));
        assertTrue(StringUtils.equalsAny("abc", "xyz", "abc"));
        assertFalse(StringUtils.equalsAny("abc", "xyz", "ABC"));

        assertFalse(StringUtils.equalsAnyIgnoreCase(null, (CharSequence[]) null));
        assertFalse(StringUtils.equalsAnyIgnoreCase("abc"));
        assertTrue(StringUtils.equalsAnyIgnoreCase("abc", "xyz", "ABC"));
        assertFalse(StringUtils.equalsAnyIgnoreCase("abc", "xyz", "def"));

        assertEquals(0, StringUtils.compare(null, null));
        assertTrue(StringUtils.compare(null, "a") < 0);
        assertTrue(StringUtils.compare("a", null) > 0);
        assertEquals(0, StringUtils.compare("abc", "abc"));
        assertTrue(StringUtils.compare("a", "b") < 0);

        assertTrue(StringUtils.compare(null, "a", false) > 0);
        assertTrue(StringUtils.compare("a", null, false) < 0);

        assertEquals(0, StringUtils.compareIgnoreCase(null, null));
        assertTrue(StringUtils.compareIgnoreCase(null, "A") < 0);
        assertTrue(StringUtils.compareIgnoreCase("A", null) > 0);
        assertEquals(0, StringUtils.compareIgnoreCase("abc", "ABC"));
        assertTrue(StringUtils.compareIgnoreCase("a", "B") < 0);

        assertTrue(StringUtils.compareIgnoreCase(null, "A", false) > 0);
        assertTrue(StringUtils.compareIgnoreCase("A", null, false) < 0);
    }

    // --- IndexOf / LastIndexOf ---
    @Test
    public void testIndexOf() {
        assertEquals(-1, StringUtils.indexOf(null, 'a'));
        assertEquals(-1, StringUtils.indexOf("", 'a'));
        assertEquals(0, StringUtils.indexOf("aabaabaa", 'a'));
        assertEquals(2, StringUtils.indexOf("aabaabaa", 'b'));
        assertEquals(-1, StringUtils.indexOf("aabaabaa", 'z'));

        assertEquals(-1, StringUtils.indexOf(null, 'a', 0));
        assertEquals(2, StringUtils.indexOf("aabaabaa", 'b', 0));
        assertEquals(5, StringUtils.indexOf("aabaabaa", 'b', 3));
        assertEquals(-1, StringUtils.indexOf("aabaabaa", 'b', 9));
        assertEquals(2, StringUtils.indexOf("aabaabaa", 'b', -1));

        assertEquals(-1, StringUtils.indexOf(null, "a"));
        assertEquals(-1, StringUtils.indexOf("a", (String) null));
        assertEquals(0, StringUtils.indexOf("", ""));
        assertEquals(0, StringUtils.indexOf("aabaabaa", "a"));
        assertEquals(2, StringUtils.indexOf("aabaabaa", "b"));
        assertEquals(1, StringUtils.indexOf("aabaabaa", "ab"));
        assertEquals(0, StringUtils.indexOf("aabaabaa", ""));

        assertEquals(-1, StringUtils.indexOf(null, "a", 0));
        assertEquals(2, StringUtils.indexOf("aabaabaa", "b", 0));
        assertEquals(5, StringUtils.indexOf("aabaabaa", "b", 3));
        assertEquals(-1, StringUtils.indexOf("aabaabaa", "b", 9));
        assertEquals(2, StringUtils.indexOf("aabaabaa", "b", -1));
        assertEquals(2, StringUtils.indexOf("aabaabaa", "", 2));

        assertEquals(-1, StringUtils.indexOfIgnoreCase(null, "a"));
        assertEquals(-1, StringUtils.indexOfIgnoreCase("a", null));
        assertEquals(0, StringUtils.indexOfIgnoreCase("", ""));
        assertEquals(0, StringUtils.indexOfIgnoreCase("aabaabaa", "A"));
        assertEquals(2, StringUtils.indexOfIgnoreCase("aabaabaa", "B"));

        assertEquals(-1, StringUtils.indexOfIgnoreCase(null, "A", 0));
        assertEquals(2, StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0));
        assertEquals(5, StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3));
        assertEquals(-1, StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9));
        assertEquals(2, StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1));
        assertEquals(2, StringUtils.indexOfIgnoreCase("aabaabaa", "", 2));
    }

    @Test
    public void testLastIndexOf() {
        assertEquals(-1, StringUtils.lastIndexOf(null, 'a'));
        assertEquals(-1, StringUtils.lastIndexOf("", 'a'));
        assertEquals(7, StringUtils.lastIndexOf("aabaabaa", 'a'));
        assertEquals(5, StringUtils.lastIndexOf("aabaabaa", 'b'));

        assertEquals(-1, StringUtils.lastIndexOf(null, 'a', 0));
        assertEquals(5, StringUtils.lastIndexOf("aabaabaa", 'b', 8));
        assertEquals(2, StringUtils.lastIndexOf("aabaabaa", 'b', 4));
        assertEquals(-1, StringUtils.lastIndexOf("aabaabaa", 'b', 0));
        assertEquals(-1, StringUtils.lastIndexOf("aabaabaa", 'b', -1));
        assertEquals(5, StringUtils.lastIndexOf("aabaabaa", 'b', 15));

        assertEquals(-1, StringUtils.lastIndexOf(null, "a"));
        assertEquals(-1, StringUtils.lastIndexOf("a", (String) null));
        assertEquals(0, StringUtils.lastIndexOf("", ""));
        assertEquals(7, StringUtils.lastIndexOf("aabaabaa", "a"));
        assertEquals(5, StringUtils.lastIndexOf("aabaabaa", "b"));
        assertEquals(4, StringUtils.lastIndexOf("aabaabaa", "ab"));
        assertEquals(8, StringUtils.lastIndexOf("aabaabaa", ""));

        assertEquals(-1, StringUtils.lastIndexOf(null, "a", 0));
        assertEquals(5, StringUtils.lastIndexOf("aabaabaa", "b", 8));
        assertEquals(2, StringUtils.lastIndexOf("aabaabaa", "b", 4));
        assertEquals(-1, StringUtils.lastIndexOf("aabaabaa", "b", -1));
        assertEquals(5, StringUtils.lastIndexOf("aabaabaa", "b", 15));

        assertEquals(-1, StringUtils.lastIndexOfIgnoreCase(null, "A"));
        assertEquals(-1, StringUtils.lastIndexOfIgnoreCase("A", null));
        assertEquals(7, StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A"));
        assertEquals(5, StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B"));

        assertEquals(-1, StringUtils.lastIndexOfIgnoreCase(null, "A", 0));
        assertEquals(5, StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 8));
        assertEquals(2, StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 4));
        assertEquals(-1, StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", -1));
        assertEquals(5, StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 15));
    }

    // --- Contains ---
    @Test
    public void testContains() {
        assertFalse(StringUtils.contains(null, 'a'));
        assertFalse(StringUtils.contains("", 'a'));
        assertTrue(StringUtils.contains("abc", 'a'));
        assertFalse(StringUtils.contains("abc", 'z'));

        assertFalse(StringUtils.contains(null, "a"));
        assertFalse(StringUtils.contains("abc", (String) null));
        assertTrue(StringUtils.contains("", ""));
        assertTrue(StringUtils.contains("abc", ""));
        assertTrue(StringUtils.contains("abc", "a"));
        assertFalse(StringUtils.contains("abc", "z"));

        assertFalse(StringUtils.containsIgnoreCase(null, "a"));
        assertFalse(StringUtils.containsIgnoreCase("abc", null));
        assertTrue(StringUtils.containsIgnoreCase("", ""));
        assertTrue(StringUtils.containsIgnoreCase("abc", "A"));
        assertFalse(StringUtils.containsIgnoreCase("abc", "Z"));

        assertFalse(StringUtils.containsWhitespace(null));
        assertFalse(StringUtils.containsWhitespace(""));
        assertFalse(StringUtils.containsWhitespace("abc"));
        assertTrue(StringUtils.containsWhitespace("a b"));
        assertTrue(StringUtils.containsWhitespace("\t"));

        assertFalse(StringUtils.containsAny(null, 'a'));
        assertFalse(StringUtils.containsAny("", 'a'));
        assertFalse(StringUtils.containsAny("abc", (char[]) null));
        assertTrue(StringUtils.containsAny("zzabyycdxx", 'z', 'a'));
        assertFalse(StringUtils.containsAny("aba", 'z'));

        assertFalse(StringUtils.containsAny(null, "a"));
        assertFalse(StringUtils.containsAny("abc", (String) null));
        assertTrue(StringUtils.containsAny("zzabyycdxx", "za"));
        assertFalse(StringUtils.containsAny("aba", "z"));

        assertFalse(StringUtils.containsAny(null, "a", "b"));
        assertFalse(StringUtils.containsAny("abc", (CharSequence[]) null));
        assertTrue(StringUtils.containsAny("abcd", "ab", "cd"));
        assertFalse(StringUtils.containsAny("abc", "d", "e"));

        assertFalse(StringUtils.containsAnyIgnoreCase(null, "a"));
        assertFalse(StringUtils.containsAnyIgnoreCase("abc", (CharSequence[]) null));
        assertTrue(StringUtils.containsAnyIgnoreCase("abcd", "AB", "cd"));
        assertFalse(StringUtils.containsAnyIgnoreCase("abc", "D", "E"));

        assertTrue(StringUtils.containsNone(null, 'a'));
        assertTrue(StringUtils.containsNone("", 'a'));
        assertTrue(StringUtils.containsNone("abc", (char[]) null));
        assertTrue(StringUtils.containsNone("ab", 'x', 'y'));
        assertFalse(StringUtils.containsNone("ab", 'a'));

        assertTrue(StringUtils.containsNone(null, "a"));
        assertTrue(StringUtils.containsNone("abc", (String) null));
        assertTrue(StringUtils.containsNone("ab", "xy"));
        assertFalse(StringUtils.containsNone("ab", "a"));

        assertFalse(StringUtils.containsOnly(null, 'a'));
        assertTrue(StringUtils.containsOnly("", 'a'));
        assertFalse(StringUtils.containsOnly("abc", (char[]) null));
        assertTrue(StringUtils.containsOnly("aba", 'a', 'b'));
        assertFalse(StringUtils.containsOnly("abz", 'a', 'b'));

        assertFalse(StringUtils.containsOnly(null, "a"));
        assertTrue(StringUtils.containsOnly("", "a"));
        assertFalse(StringUtils.containsOnly("abc", (String) null));
        assertTrue(StringUtils.containsOnly("aba", "ab"));
        assertFalse(StringUtils.containsOnly("abz", "ab"));
    }

    // --- IndexOfAny ---
    @Test
    public void testIndexOfAny() {
        assertEquals(-1, StringUtils.indexOfAny(null, 'a'));
        assertEquals(-1, StringUtils.indexOfAny("", 'a'));
        assertEquals(-1, StringUtils.indexOfAny("abc", (char[]) null));
        assertEquals(0, StringUtils.indexOfAny("zzabyycdxx", 'z', 'a'));
        assertEquals(3, StringUtils.indexOfAny("zzabyycdxx", 'b', 'y'));
        assertEquals(-1, StringUtils.indexOfAny("aba", 'z'));

        assertEquals(-1, StringUtils.indexOfAny(null, "a"));
        assertEquals(-1, StringUtils.indexOfAny("abc", (String) null));
        assertEquals(0, StringUtils.indexOfAny("zzabyycdxx", "za"));

        assertEquals(-1, StringUtils.indexOfAny(null, 0, 'a'));
        assertEquals(-1, StringUtils.indexOfAny("abc", 0, (char[]) null));
        assertEquals(3, StringUtils.indexOfAny("zzabyycdxx", 2, 'b', 'y'));

        assertEquals(-1, StringUtils.indexOfAny(null, "a", "b"));
        assertEquals(-1, StringUtils.indexOfAny("abc", (CharSequence[]) null));
        assertEquals(2, StringUtils.indexOfAny("zzabyycdxx", "ab", "cd"));

        assertEquals(-1, StringUtils.indexOfAnyBut(null, 'a'));
        assertEquals(-1, StringUtils.indexOfAnyBut("", 'a'));
        assertEquals(-1, StringUtils.indexOfAnyBut("abc", (char[]) null));
        assertEquals(3, StringUtils.indexOfAnyBut("zzza", 'z'));
        assertEquals(-1, StringUtils.indexOfAnyBut("aba", 'a', 'b'));

        assertEquals(-1, StringUtils.indexOfAnyBut(null, "a"));
        assertEquals(-1, StringUtils.indexOfAnyBut("abc", (CharSequence) null));
        assertEquals(3, StringUtils.indexOfAnyBut("zzza", "z"));

        assertEquals(-1, StringUtils.indexOfDifference(null, null));
        assertEquals(0, StringUtils.indexOfDifference(null, "i"));
        assertEquals(0, StringUtils.indexOfDifference("i", null));
        assertEquals(-1, StringUtils.indexOfDifference("abc", "abc"));
        assertEquals(2, StringUtils.indexOfDifference("abc", "abd"));

        assertEquals(-1, StringUtils.indexOfDifference((CharSequence[]) null));
        assertEquals(-1, StringUtils.indexOfDifference(new CharSequence[0]));
        assertEquals(-1, StringUtils.indexOfDifference("abc", "abc"));
        assertEquals(2, StringUtils.indexOfDifference("abc", "abd"));

        assertNull(StringUtils.difference(null, null));
        assertEquals("i", StringUtils.difference(null, "i"));
        assertEquals("i", StringUtils.difference("i", null));
        assertEquals("robot", StringUtils.difference("i am a machine", "i am a robot"));

        assertEquals("", StringUtils.getCommonPrefix((String[]) null));
        assertEquals("", StringUtils.getCommonPrefix(new String[0]));
        assertEquals("abc", StringUtils.getCommonPrefix("abc"));
        assertEquals("", StringUtils.getCommonPrefix("abc", null));
        assertEquals("ab", StringUtils.getCommonPrefix("abc", "abd", "abx"));
    }

    // --- Substring ---
    @Test
    public void testSubstring() {
        assertNull(StringUtils.substring(null, 0));
        assertEquals("", StringUtils.substring("", 0));
        assertEquals("abc", StringUtils.substring("abc", 0));
        assertEquals("bc", StringUtils.substring("abc", 1));
        assertEquals("c", StringUtils.substring("abc", -1));
        assertEquals("abc", StringUtils.substring("abc", -4));

        assertNull(StringUtils.substring(null, 0, 1));
        assertEquals("", StringUtils.substring("", 0, 1));
        assertEquals("ab", StringUtils.substring("abc", 0, 2));
        assertEquals("", StringUtils.substring("abc", 2, 0));
        assertEquals("b", StringUtils.substring("abc", 1, -1));
        assertEquals("", StringUtils.substring("abc", -1, -2));

        assertNull(StringUtils.left(null, 2));
        assertEquals("", StringUtils.left("abc", -1));
        assertEquals("", StringUtils.left("abc", 0));
        assertEquals("ab", StringUtils.left("abc", 2));
        assertEquals("abc", StringUtils.left("abc", 4));

        assertNull(StringUtils.right(null, 2));
        assertEquals("", StringUtils.right("abc", -1));
        assertEquals("", StringUtils.right("abc", 0));
        assertEquals("bc", StringUtils.right("abc", 2));
        assertEquals("abc", StringUtils.right("abc", 4));

        assertNull(StringUtils.mid(null, 0, 2));
        assertEquals("", StringUtils.mid("abc", 0, -1));
        assertEquals("", StringUtils.mid("abc", 4, 2));
        assertEquals("ab", StringUtils.mid("abc", 0, 2));
        assertEquals("bc", StringUtils.mid("abc", 1, 2));
        assertEquals("ab", StringUtils.mid("abc", -1, 2));

        assertNull(StringUtils.substringBefore(null, ";"));
        assertEquals("", StringUtils.substringBefore("", ";"));
        assertEquals("abc", StringUtils.substringBefore("abc", null));
        assertEquals("foo", StringUtils.substringBefore("foo;bar", ";"));
        assertEquals("abc", StringUtils.substringBefore("abc", ";"));
        assertEquals("foo", StringUtils.substringBefore("foo;bar", ';'));

        assertNull(StringUtils.substringAfter(null, ";"));
        assertEquals("", StringUtils.substringAfter("", ";"));
        assertEquals("", StringUtils.substringAfter("abc", null));
        assertEquals("bar", StringUtils.substringAfter("foo;bar", ";"));
        assertEquals("", StringUtils.substringAfter("abc", ";"));
        assertEquals("bar", StringUtils.substringAfter("foo;bar", ';'));

        assertNull(StringUtils.substringBeforeLast(null, ";"));
        assertEquals("", StringUtils.substringBeforeLast("", ";"));
        assertEquals("abc", StringUtils.substringBeforeLast("abc", null));
        assertEquals("foo;bar", StringUtils.substringBeforeLast("foo;bar;baz", ";"));
        assertEquals("abc", StringUtils.substringBeforeLast("abc", ";"));

        assertNull(StringUtils.substringAfterLast(null, ";"));
        assertEquals("", StringUtils.substringAfterLast("", ";"));
        assertEquals("", StringUtils.substringAfterLast("abc", null));
        assertEquals("baz", StringUtils.substringAfterLast("foo;bar;baz", ";"));
        assertEquals("", StringUtils.substringAfterLast("abc", ";"));
        assertEquals("baz", StringUtils.substringAfterLast("foo;bar;baz", ';'));

        assertNull(StringUtils.substringBetween(null, "tag"));
        assertEquals("", StringUtils.substringBetween("", ""));
        assertNull(StringUtils.substringBetween("abc", null));
        assertNull(StringUtils.substringBetween("abc", "tag"));
        assertEquals("bar", StringUtils.substringBetween("foo[bar]baz", "[", "]"));

        assertNull(StringUtils.substringsBetween(null, "[", "]"));
        assertNull(StringUtils.substringsBetween("abc", null, "]"));
        assertNull(StringUtils.substringsBetween("abc", "[", null));
        assertArrayEquals(new String[0], StringUtils.substringsBetween("", "[", "]"));
        assertNull(StringUtils.substringsBetween("abc", "[", "]"));
        assertArrayEquals(new String[] {"1", "2"}, StringUtils.substringsBetween("[1] [2]", "[", "]"));
    }

    // --- Split ---
    @Test
    public void testSplit() {
        assertNull(StringUtils.split(null));
        assertArrayEquals(new String[0], StringUtils.split(""));
        assertArrayEquals(new String[] {"abc", "def"}, StringUtils.split("abc def"));
        assertArrayEquals(new String[] {"abc", "def"}, StringUtils.split("abc  def"));

        assertArrayEquals(new String[] {"a", "b", "c"}, StringUtils.split("a.b.c", '.'));
        assertArrayEquals(new String[] {"a", "b", "c"}, StringUtils.split("a..b.c", '.'));

        assertArrayEquals(new String[] {"a", "b", "c"}, StringUtils.split("a;b.c", ";."));
        assertArrayEquals(new String[] {"a", "b.c"}, StringUtils.split("a;b.c", ";.", 2));
        assertArrayEquals(new String[] {"a", "b", "c"}, StringUtils.split("a;b.c", ";.", 0));
        assertArrayEquals(new String[] {"a", "b", "c"}, StringUtils.split("a;b.c", ";.", -1));

        assertNull(StringUtils.splitPreserveAllTokens(null));
        assertArrayEquals(new String[0], StringUtils.splitPreserveAllTokens(""));
        assertArrayEquals(new String[] {"a", "", "b"}, StringUtils.splitPreserveAllTokens("a..b", '.'));
        assertArrayEquals(new String[] {"a", "", "b"}, StringUtils.splitPreserveAllTokens("a,,b", ","));
        assertArrayEquals(new String[] {"a", ",b"}, StringUtils.splitPreserveAllTokens("a,,b", ",", 2));

        assertNull(StringUtils.splitByWholeSeparator(null, "."));
        assertArrayEquals(new String[0], StringUtils.splitByWholeSeparator("", "."));
        assertArrayEquals(new String[] {"ab", "cd"}, StringUtils.splitByWholeSeparator("ab--cd", "--"));
        assertArrayEquals(new String[] {"ab", "cd--ef"}, StringUtils.splitByWholeSeparator("ab--cd--ef", "--", 2));
        assertArrayEquals(new String[] {"ab", "cd"}, StringUtils.splitByWholeSeparator("ab cd", null));

        assertArrayEquals(new String[] {"ab", "", "cd"}, StringUtils.splitByWholeSeparatorPreserveAllTokens("ab----cd", "--"));
        assertArrayEquals(new String[] {"ab", "--cd"}, StringUtils.splitByWholeSeparatorPreserveAllTokens("ab----cd", "--", 2));

        assertNull(StringUtils.splitByCharacterType(null));
        assertArrayEquals(new String[0], StringUtils.splitByCharacterType(""));
        assertArrayEquals(new String[] {"ab", "C", "12", " "}, StringUtils.splitByCharacterType("abC12 "));

        assertNull(StringUtils.splitByCharacterTypeCamelCase(null));
        assertArrayEquals(new String[0], StringUtils.splitByCharacterTypeCamelCase(""));
        assertArrayEquals(new String[] {"ab", "C", "De"}, StringUtils.splitByCharacterTypeCamelCase("abCDe"));
    }

    // --- Join ---
    @Test
    public void testJoin() {
        assertNull(StringUtils.join((Object[]) null, ","));
        assertEquals("", StringUtils.join(new Object[0], ","));
        assertEquals("a,b,c", StringUtils.join(new Object[] {"a", "b", "c"}, ","));
        assertEquals("a,b,c", StringUtils.join(new Object[] {"a", "b", "c"}, ','));
        assertEquals("abc", StringUtils.join(new Object[] {"a", "b", "c"}, null));

        assertEquals("b,c", StringUtils.join(new Object[] {"a", "b", "c", "d"}, ",", 1, 3));
        assertEquals("b,c", StringUtils.join(new Object[] {"a", "b", "c", "d"}, ',', 1, 3));

        assertNull(StringUtils.join((Iterable<?>) null, ","));
        assertEquals("", StringUtils.join(Collections.emptyList(), ","));
        assertEquals("1,2", StringUtils.join(Arrays.asList(1, 2), ","));
        assertEquals("1,2", StringUtils.join(Arrays.asList(1, 2), ','));

        assertNull(StringUtils.join((Iterator<?>) null, ","));
        assertEquals("1,2", StringUtils.join(Arrays.asList(1, 2).iterator(), ","));
        assertEquals("1,2", StringUtils.join(Arrays.asList(1, 2).iterator(), ','));

        assertNull(StringUtils.join((List<?>) null, ',', 0, 1));
        assertNull(StringUtils.join((List<?>) null, ",", 0, 1));
        assertEquals("2,3", StringUtils.join(Arrays.asList(1, 2, 3, 4), ',', 1, 3));
        assertEquals("2,3", StringUtils.join(Arrays.asList(1, 2, 3, 4), ",", 1, 3));

        assertEquals("a,b", StringUtils.joinWith(",", "a", "b"));
        assertEquals("", StringUtils.joinWith(",", new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.joinWith(",", (Object[]) null));
    }

    // --- Delete / Remove ---
    @Test
    public void testDeleteRemove() {
        assertNull(StringUtils.deleteWhitespace(null));
        assertEquals("", StringUtils.deleteWhitespace(""));
        assertEquals("abc", StringUtils.deleteWhitespace("  a  b  c  \t\n "));

        assertNull(StringUtils.remove(null, "a"));
        assertEquals("", StringUtils.remove("", "a"));
        assertEquals("abc", StringUtils.remove("abc", null));
        assertEquals("abc", StringUtils.remove("abc", ""));
        assertEquals("queued", StringUtils.remove("queued", "z"));
        assertEquals("qd", StringUtils.remove("queued", "ue"));
        assertEquals("quud", StringUtils.remove("queued", 'e'));

        assertNull(StringUtils.removeStart(null, "a"));
        assertEquals("", StringUtils.removeStart("", "a"));
        assertEquals("abc", StringUtils.removeStart("abc", null));
        assertEquals("domain.com", StringUtils.removeStart("www.domain.com", "www."));
        assertEquals("domain.com", StringUtils.removeStart("domain.com", "www."));

        assertNull(StringUtils.removeStartIgnoreCase(null, "a"));
        assertEquals("domain.com", StringUtils.removeStartIgnoreCase("WWW.domain.com", "www."));

        assertNull(StringUtils.removeEnd(null, "a"));
        assertEquals("", StringUtils.removeEnd("", "a"));
        assertEquals("abc", StringUtils.removeEnd("abc", null));
        assertEquals("domain", StringUtils.removeEnd("domain.com", ".com"));
        assertEquals("domain.com", StringUtils.removeEnd("domain.com", ".org"));

        assertNull(StringUtils.removeEndIgnoreCase(null, "a"));
        assertEquals("domain", StringUtils.removeEndIgnoreCase("domain.COM", ".com"));

        assertNull(StringUtils.removeAll(null, "a"));
        assertEquals("", StringUtils.removeAll("", "a"));
        assertEquals("abc", StringUtils.removeAll("a1b2c", "\\d"));

        assertNull(StringUtils.removeFirst(null, "a"));
        assertEquals("b2c", StringUtils.removeFirst("a1b2c", "[a-z]\\d"));

        assertNull(StringUtils.removePattern(null, "a"));
        assertEquals("abc", StringUtils.removePattern("a1b2c", "\\d"));
    }

    // --- Replace ---
    @Test
    public void testReplace() {
        assertNull(StringUtils.replace(null, "a", "b"));
        assertEquals("", StringUtils.replace("", "a", "b"));
        assertEquals("aba", StringUtils.replace("aba", null, "b"));
        assertEquals("aba", StringUtils.replace("aba", "", "b"));
        assertEquals("aba", StringUtils.replace("aba", "a", null));
        assertEquals("zbz", StringUtils.replace("aba", "a", "z"));
        assertEquals("zba", StringUtils.replace("aba", "a", "z", 1));
        assertEquals("aba", StringUtils.replace("aba", "a", "z", 0));

        assertEquals("zba", StringUtils.replaceOnce("aba", "a", "z"));

        assertNull(StringUtils.replaceIgnoreCase(null, "a", "b"));
        assertEquals("zbz", StringUtils.replaceIgnoreCase("AbA", "a", "z"));
        assertEquals("zbA", StringUtils.replaceOnceIgnoreCase("AbA", "a", "z"));

        assertNull(StringUtils.replacePattern(null, "a", "b"));
        assertEquals("a#b#c", StringUtils.replacePattern("a1b2c", "\\d", "#"));

        assertNull(StringUtils.replaceFirst(null, "a", "b"));
        assertEquals("a#b2c", StringUtils.replaceFirst("a1b2c", "\\d", "#"));

        assertNull(StringUtils.replaceAll(null, "a", "b"));
        assertEquals("a#b#c", StringUtils.replaceAll("a1b2c", "\\d", "#"));

        assertNull(StringUtils.replaceChars(null, 'a', 'b'));
        assertEquals("zbz", StringUtils.replaceChars("aba", 'a', 'z'));

        assertNull(StringUtils.replaceChars(null, "a", "b"));
        assertEquals("zbz", StringUtils.replaceChars("aba", "a", "z"));
        assertEquals("b", StringUtils.replaceChars("aba", "a", ""));
        assertEquals("aba", StringUtils.replaceChars("aba", null, "z"));

        assertNull(StringUtils.replaceEach(null, new String[] {"a"}, new String[] {"b"}));
        assertEquals("", StringUtils.replaceEach("", new String[] {"a"}, new String[] {"b"}));
        assertEquals("bcc", StringUtils.replaceEach("abc", new String[] {"a", "b"}, new String[] {"b", "c"}));
        assertEquals("bbb", StringUtils.replaceEachRepeatedly("aba", new String[] {"a"}, new String[] {"b"}));

        assertNull(StringUtils.overlay(null, "a", 0, 1));
        assertEquals("", StringUtils.overlay("", "", 0, 0));
        assertEquals("abcdef", StringUtils.overlay("abef", "cd", 2, 2));
        assertEquals("abcdef", StringUtils.overlay("abzzef", "cd", 2, 4));
        assertEquals("abcdef", StringUtils.overlay("abzzef", "cd", 4, 2));
        assertEquals("cd", StringUtils.overlay("ab", "cd", -1, 5));
    }

    // --- Case ---
    @Test
    public void testCase() {
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("Cat", StringUtils.capitalize("cat"));
        assertEquals("Cat", StringUtils.capitalize("Cat"));

        assertNull(StringUtils.uncapitalize(null));
        assertEquals("", StringUtils.uncapitalize(""));
        assertEquals("cat", StringUtils.uncapitalize("Cat"));

        assertNull(StringUtils.upperCase(null));
        assertEquals("ABC", StringUtils.upperCase("abc"));
        assertEquals("ABC", StringUtils.upperCase("abc", Locale.ENGLISH));

        assertNull(StringUtils.lowerCase(null));
        assertEquals("abc", StringUtils.lowerCase("ABC"));
        assertEquals("abc", StringUtils.lowerCase("ABC", Locale.ENGLISH));

        assertNull(StringUtils.swapCase(null));
        assertEquals("", StringUtils.swapCase(""));
        assertEquals("tEST cASE", StringUtils.swapCase("Test Case"));

        assertNull(StringUtils.toRootLowerCase(null));
        assertEquals("abc", StringUtils.toRootLowerCase("ABC"));
        assertNull(StringUtils.toRootUpperCase(null));
        assertEquals("ABC", StringUtils.toRootUpperCase("abc"));

        assertFalse(StringUtils.isAllLowerCase(null));
        assertFalse(StringUtils.isAllLowerCase(""));
        assertTrue(StringUtils.isAllLowerCase("abc"));
        assertFalse(StringUtils.isAllLowerCase("aBc"));

        assertFalse(StringUtils.isAllUpperCase(null));
        assertFalse(StringUtils.isAllUpperCase(""));
        assertTrue(StringUtils.isAllUpperCase("ABC"));
        assertFalse(StringUtils.isAllUpperCase("aBC"));

        assertFalse(StringUtils.isMixedCase(null));
        assertFalse(StringUtils.isMixedCase(""));
        assertFalse(StringUtils.isMixedCase("abc"));
        assertFalse(StringUtils.isMixedCase("ABC"));
        assertTrue(StringUtils.isMixedCase("aBc"));
    }

    // --- Is Checks ---
    @Test
    public void testIs() {
        assertFalse(StringUtils.isAlpha(null));
        assertFalse(StringUtils.isAlpha(""));
        assertTrue(StringUtils.isAlpha("abc"));
        assertFalse(StringUtils.isAlpha("ab1"));

        assertFalse(StringUtils.isAlphaSpace(null));
        assertTrue(StringUtils.isAlphaSpace(""));
        assertTrue(StringUtils.isAlphaSpace("abc def"));
        assertFalse(StringUtils.isAlphaSpace("ab1"));

        assertFalse(StringUtils.isAlphanumeric(null));
        assertFalse(StringUtils.isAlphanumeric(""));
        assertTrue(StringUtils.isAlphanumeric("ab12"));
        assertFalse(StringUtils.isAlphanumeric("ab 12"));

        assertFalse(StringUtils.isAlphanumericSpace(null));
        assertTrue(StringUtils.isAlphanumericSpace(""));
        assertTrue(StringUtils.isAlphanumericSpace("ab 12"));
        assertFalse(StringUtils.isAlphanumericSpace("ab_12"));

        assertFalse(StringUtils.isAsciiPrintable(null));
        assertTrue(StringUtils.isAsciiPrintable(""));
        assertTrue(StringUtils.isAsciiPrintable("Hello 123!"));
        assertFalse(StringUtils.isAsciiPrintable("\u0000"));

        assertFalse(StringUtils.isNumeric(null));
        assertFalse(StringUtils.isNumeric(""));
        assertTrue(StringUtils.isNumeric("123"));
        assertFalse(StringUtils.isNumeric("12a3"));

        assertFalse(StringUtils.isNumericSpace(null));
        assertTrue(StringUtils.isNumericSpace(""));
        assertTrue(StringUtils.isNumericSpace("12 3"));
        assertFalse(StringUtils.isNumericSpace("12a3"));

        assertFalse(StringUtils.isWhitespace(null));
        assertTrue(StringUtils.isWhitespace(""));
        assertTrue(StringUtils.isWhitespace("  \t\n "));
        assertFalse(StringUtils.isWhitespace(" a "));
    }

    // --- Pad / Center / Abbreviate ---
    @Test
    public void testPadCenterAbbreviate() {
        assertEquals("", StringUtils.repeat('a', 0));
        assertEquals("aaa", StringUtils.repeat('a', 3));
        assertNull(StringUtils.repeat(null, 2));
        assertEquals("", StringUtils.repeat("abc", 0));
        assertEquals("abcabc", StringUtils.repeat("abc", 2));
        assertEquals("a,a", StringUtils.repeat("a", ",", 2));

        assertNull(StringUtils.rightPad(null, 5));
        assertEquals("abc  ", StringUtils.rightPad("abc", 5));
        assertEquals("abc00", StringUtils.rightPad("abc", 5, '0'));
        assertEquals("abcxyz", StringUtils.rightPad("abc", 6, "xyz"));
        assertEquals("abc", StringUtils.rightPad("abc", 2));

        assertNull(StringUtils.leftPad(null, 5));
        assertEquals("  abc", StringUtils.leftPad("abc", 5));
        assertEquals("00abc", StringUtils.leftPad("abc", 5, '0'));
        assertEquals("xyzabc", StringUtils.leftPad("abc", 6, "xyz"));
        assertEquals("abc", StringUtils.leftPad("abc", 2));

        assertNull(StringUtils.center(null, 5));
        assertEquals(" abc ", StringUtils.center("abc", 5));
        assertEquals("0abc0", StringUtils.center("abc", 5, '0'));
        assertEquals("xabcx", StringUtils.center("abc", 5, "xy"));
        assertEquals("abc", StringUtils.center("abc", 2));

        assertNull(StringUtils.abbreviate(null, 4));
        assertEquals("abc", StringUtils.abbreviate("abc", 4));
        assertEquals("a...", StringUtils.abbreviate("abcdefg", 4));
        assertEquals("...f...", StringUtils.abbreviate("abcdefghij", 5, 7));
        assertEquals("a*", StringUtils.abbreviate("abcde", "*", 2));
        assertEquals("*d*", StringUtils.abbreviate("abcdef", "*", 3, 3));

        assertNull(StringUtils.abbreviateMiddle(null, "..", 4));
        assertEquals("abc", StringUtils.abbreviateMiddle("abc", "..", 4));
        assertEquals("abcd", StringUtils.abbreviateMiddle("abcd", "..", 4));
        assertEquals("a..e", StringUtils.abbreviateMiddle("abcde", "..", 4));

        assertNull(StringUtils.truncate(null, 4));
        assertEquals("abc", StringUtils.truncate("abc", 4));
        assertEquals("abcd", StringUtils.truncate("abcdef", 4));
        assertEquals("cde", StringUtils.truncate("abcdef", 2, 3));
    }

    // --- StartsWith / EndsWith / Wrap ---
    @Test
    public void testStartsEndsWithWrap() {
        assertTrue(StringUtils.startsWith(null, null));
        assertFalse(StringUtils.startsWith(null, "abc"));
        assertFalse(StringUtils.startsWith("abc", null));
        assertTrue(StringUtils.startsWith("abcdef", "abc"));
        assertFalse(StringUtils.startsWith("abcdef", "xyz"));

        assertTrue(StringUtils.startsWithIgnoreCase("ABCDEF", "abc"));

        assertFalse(StringUtils.startsWithAny(null, "abc"));
        assertTrue(StringUtils.startsWithAny("abcdef", "xyz", "abc"));

        assertTrue(StringUtils.endsWith(null, null));
        assertTrue(StringUtils.endsWith("abcdef", "def"));
        assertFalse(StringUtils.endsWith("abcdef", "xyz"));

        assertTrue(StringUtils.endsWithIgnoreCase("ABCDEF", "def"));

        assertFalse(StringUtils.endsWithAny(null, "def"));
        assertTrue(StringUtils.endsWithAny("abcdef", "xyz", "def"));

        assertEquals("abcxyz", StringUtils.appendIfMissing("abc", "xyz"));
        assertEquals("abcxyz", StringUtils.appendIfMissing("abcxyz", "xyz"));
        assertEquals("abcXYZ", StringUtils.appendIfMissingIgnoreCase("abcXYZ", "xyz"));

        assertEquals("xyzabc", StringUtils.prependIfMissing("abc", "xyz"));
        assertEquals("xyzabc", StringUtils.prependIfMissing("xyzabc", "xyz"));
        assertEquals("XYZabc", StringUtils.prependIfMissingIgnoreCase("XYZabc", "xyz"));

        assertNull(StringUtils.wrap(null, '"'));
        assertEquals("\"abc\"", StringUtils.wrap("abc", '"'));
        assertEquals("'abc'", StringUtils.wrap("abc", "'"));

        assertEquals("\"abc\"", StringUtils.wrapIfMissing("abc", '"'));
        assertEquals("\"abc\"", StringUtils.wrapIfMissing("\"abc\"", '"'));
        assertEquals("'abc'", StringUtils.wrapIfMissing("abc", "'"));

        assertNull(StringUtils.unwrap(null, '"'));
        assertEquals("abc", StringUtils.unwrap("\"abc\"", '"'));
        assertEquals("abc", StringUtils.unwrap("'abc'", "'"));
        assertEquals("abc", StringUtils.unwrap("abc", '"'));
    }

    // --- Misc ---
    @Test
    public void testMisc() throws UnsupportedEncodingException {
        assertEquals(0, StringUtils.countMatches(null, 'a'));
        assertEquals(0, StringUtils.countMatches("", 'a'));
        assertEquals(3, StringUtils.countMatches("abbaa", 'a'));

        assertEquals(0, StringUtils.countMatches(null, "a"));
        assertEquals(1, StringUtils.countMatches("abbaa", "ab"));

        assertNull(StringUtils.chomp(null));
        assertEquals("", StringUtils.chomp(""));
        assertEquals("abc", StringUtils.chomp("abc\r\n"));
        assertEquals("abc", StringUtils.chomp("abc\n"));
        assertEquals("abc", StringUtils.chomp("abc\r"));
        assertEquals("abc", StringUtils.chomp("abc"));
        assertEquals("foo", StringUtils.chomp("foobar", "bar"));

        assertNull(StringUtils.chop(null));
        assertEquals("", StringUtils.chop(""));
        assertEquals("abc", StringUtils.chop("abc\r\n"));
        assertEquals("abc", StringUtils.chop("abcd"));

        assertNull(StringUtils.reverse(null));
        assertEquals("cba", StringUtils.reverse("abc"));

        assertNull(StringUtils.reverseDelimited(null, '.'));
        assertEquals("c.b.a", StringUtils.reverseDelimited("a.b.c", '.'));

        assertNull(StringUtils.rotate(null, 1));
        assertEquals("eabcd", StringUtils.rotate("abcde", 1));
        assertEquals("bcdea", StringUtils.rotate("abcde", -1));

        assertArrayEquals(new byte[0], StringUtils.getBytes(null, StandardCharsets.UTF_8));
        assertNotNull(StringUtils.getBytes("abc", StandardCharsets.UTF_8));
        assertNotNull(StringUtils.getBytes("abc", "UTF-8"));

        assertThrows(NullPointerException.class, () -> StringUtils.toEncodedString(null, StandardCharsets.UTF_8));
        assertEquals("abc", StringUtils.toEncodedString("abc".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));

        assertThrows(NullPointerException.class, () -> StringUtils.toString(null, "UTF-8"));
        assertEquals("abc", StringUtils.toString("abc".getBytes(StandardCharsets.UTF_8), "UTF-8"));

        assertNull(StringUtils.toCodePoints(null));
        assertArrayEquals(new int[0], StringUtils.toCodePoints(""));
        assertArrayEquals(new int[] {97, 98, 99}, StringUtils.toCodePoints("abc"));

        assertNull(StringUtils.getDigits(null));
        assertEquals("", StringUtils.getDigits("abc"));
        assertEquals("123", StringUtils.getDigits("a1b2c3"));

        assertEquals(0, StringUtils.getLevenshteinDistance("", ""));
        assertEquals(1, StringUtils.getLevenshteinDistance("a", "b"));
        assertEquals(1, StringUtils.getLevenshteinDistance("a", "b", 2));
        assertEquals(-1, StringUtils.getLevenshteinDistance("a", "bbb", 1));

        assertEquals(1.0, StringUtils.getJaroWinklerDistance("abc", "abc"), 0.001);
        assertTrue(StringUtils.getJaroWinklerDistance("abc", "abd") > 0);

        assertEquals(0, StringUtils.getFuzzyDistance("", "", Locale.ENGLISH));
        assertTrue(StringUtils.getFuzzyDistance("apache", "apc", Locale.ENGLISH) > 0);

        assertNull(StringUtils.valueOf(null));
        assertEquals("abc", StringUtils.valueOf(new char[] {'a', 'b', 'c'}));
    }

    @Test
    public void testAdditionalCoverage() {
        // join primitives
        assertNull(StringUtils.join((boolean[]) null, ';'));
        assertEquals("true;false", StringUtils.join(new boolean[] {true, false}, ';'));
        assertEquals("true;false", StringUtils.join(new boolean[] {true, false, true}, ';', 0, 2));
        assertEquals("", StringUtils.join(new boolean[] {true, false}, ';', 1, 1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> StringUtils.join(new boolean[] {true}, ';', -1, 1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> StringUtils.join(new boolean[] {true}, ';', 0, 2));

        assertNull(StringUtils.join((byte[]) null, ';'));
        assertEquals("1;2", StringUtils.join(new byte[] {1, 2}, ';'));
        assertEquals("1;2", StringUtils.join(new byte[] {1, 2, 3}, ';', 0, 2));
        assertEquals("", StringUtils.join(new byte[] {1, 2}, ';', 1, 1));

        assertNull(StringUtils.join((char[]) null, ';'));
        assertEquals("a;b", StringUtils.join(new char[] {'a', 'b'}, ';'));
        assertEquals("a;b", StringUtils.join(new char[] {'a', 'b', 'c'}, ';', 0, 2));
        assertEquals("", StringUtils.join(new char[] {'a', 'b'}, ';', 1, 1));

        assertNull(StringUtils.join((double[]) null, ';'));
        assertEquals("1.0;2.0", StringUtils.join(new double[] {1.0, 2.0}, ';'));
        assertEquals("1.0;2.0", StringUtils.join(new double[] {1.0, 2.0, 3.0}, ';', 0, 2));
        assertEquals("", StringUtils.join(new double[] {1.0, 2.0}, ';', 1, 1));

        assertNull(StringUtils.join((float[]) null, ';'));
        assertEquals("1.0;2.0", StringUtils.join(new float[] {1.0f, 2.0f}, ';'));
        assertEquals("1.0;2.0", StringUtils.join(new float[] {1.0f, 2.0f, 3.0f}, ';', 0, 2));
        assertEquals("", StringUtils.join(new float[] {1.0f, 2.0f}, ';', 1, 1));

        assertNull(StringUtils.join((int[]) null, ';'));
        assertEquals("1;2", StringUtils.join(new int[] {1, 2}, ';'));
        assertEquals("1;2", StringUtils.join(new int[] {1, 2, 3}, ';', 0, 2));
        assertEquals("", StringUtils.join(new int[] {1, 2}, ';', 1, 1));

        assertNull(StringUtils.join((long[]) null, ';'));
        assertEquals("1;2", StringUtils.join(new long[] {1L, 2L}, ';'));
        assertEquals("1;2", StringUtils.join(new long[] {1L, 2L, 3L}, ';', 0, 2));
        assertEquals("", StringUtils.join(new long[] {1L, 2L}, ';', 1, 1));

        assertNull(StringUtils.join((short[]) null, ';'));
        assertEquals("1;2", StringUtils.join(new short[] {1, 2}, ';'));
        assertEquals("1;2", StringUtils.join(new short[] {1, 2, 3}, ';', 0, 2));
        assertEquals("", StringUtils.join(new short[] {1, 2}, ';', 1, 1));

        assertNull(StringUtils.join((Object[]) null));
        assertEquals("abc", StringUtils.join(new Object[] {"a", "b", "c"}));
        assertNull(StringUtils.join((Iterable<?>) null, ';'));
        assertEquals("", StringUtils.join(java.util.Collections.emptyList(), ';'));
        assertEquals("a;b", StringUtils.join(java.util.Arrays.asList("a", "b"), ';'));
        assertNull(StringUtils.join((java.util.Iterator<?>) null, ';'));
        assertEquals("a;b", StringUtils.join(java.util.Arrays.asList("a", "b").iterator(), ';'));
        assertEquals("a;b", StringUtils.join(java.util.Arrays.asList("a", "b", "c"), ';', 0, 2));
        assertEquals("a,b", StringUtils.join(java.util.Arrays.asList("a", "b", "c"), ",", 0, 2));
        assertEquals("a,b", StringUtils.join(new Object[] {"a", "b", "c"}, ",", 0, 2));

        // normalizeSpace
        assertNull(StringUtils.normalizeSpace(null));
        assertEquals("", StringUtils.normalizeSpace(""));
        assertEquals("a b c d", StringUtils.normalizeSpace("   a   b \t c \n d  "));

        // ordinalIndexOf / lastOrdinalIndexOf
        assertEquals(-1, StringUtils.ordinalIndexOf(null, "a", 1));
        assertEquals(-1, StringUtils.ordinalIndexOf("a", null, 1));
        assertEquals(-1, StringUtils.ordinalIndexOf("a", "a", -1));
        assertEquals(-1, StringUtils.ordinalIndexOf("a", "a", 0));
        assertEquals(0, StringUtils.ordinalIndexOf("", "", 1));
        assertEquals(0, StringUtils.ordinalIndexOf("", "", 2));
        assertEquals(2, StringUtils.ordinalIndexOf("aabaabaa", "b", 1));
        assertEquals(5, StringUtils.ordinalIndexOf("aabaabaa", "b", 2));
        assertEquals(-1, StringUtils.ordinalIndexOf("aabaabaa", "b", 3));

        assertEquals(-1, StringUtils.lastOrdinalIndexOf(null, "a", 1));
        assertEquals(-1, StringUtils.lastOrdinalIndexOf("a", null, 1));
        assertEquals(-1, StringUtils.lastOrdinalIndexOf("a", "a", -1));
        assertEquals(5, StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 1));
        assertEquals(2, StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 2));
        assertEquals(-1, StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 3));

        // lastIndexOfAny
        assertEquals(-1, StringUtils.lastIndexOfAny(null, "a"));
        assertEquals(-1, StringUtils.lastIndexOfAny("abc", (CharSequence[]) null));
        assertEquals(-1, StringUtils.lastIndexOfAny("abc"));
        assertEquals(6, StringUtils.lastIndexOfAny("zzabyycdxx", "ab", "cd"));
        assertEquals(6, StringUtils.lastIndexOfAny("zzabyycdxx", "cd", "ab"));
        assertEquals(-1, StringUtils.lastIndexOfAny("zzabyycdxx", (CharSequence) null));

        // removeIgnoreCase / removeStart
        assertNull(StringUtils.removeIgnoreCase(null, "a"));
        assertEquals("", StringUtils.removeIgnoreCase("", "a"));
        assertEquals("qd", StringUtils.removeIgnoreCase("queued", "UE"));
        assertNull(StringUtils.removeStart(null, 'w'));
        assertEquals("", StringUtils.removeStart("", 'w'));
        assertEquals("ww.domain.com", StringUtils.removeStart("www.domain.com", 'w'));
        assertEquals("domain.com", StringUtils.removeStart("domain.com", 'w'));

        // replaceIgnoreCase
        assertNull(StringUtils.replaceIgnoreCase(null, "a", "b", 1));
        assertEquals("aba", StringUtils.replaceIgnoreCase("aba", null, "b", 1));
        assertEquals("aba", StringUtils.replaceIgnoreCase("aba", "", "b", 1));
        assertEquals("aba", StringUtils.replaceIgnoreCase("aba", "a", null, 1));
        assertEquals("AbA", StringUtils.replaceIgnoreCase("AbA", "a", "z", 0));
        assertEquals("zbA", StringUtils.replaceIgnoreCase("AbA", "a", "z", 1));
        assertEquals("zbz", StringUtils.replaceIgnoreCase("AbA", "a", "z", -1));

        // Accents
        assertEquals("LlDdIiIiIUuUuTt", StringUtils.stripAccents("\u0141\u0142\u0110\u0111\u0197\u0268\u1D7B\u1DA4\u1DA7\u0244\u0289\u1D7E\u1DB6\u0166\u0167"));

        // Pad branches
        assertEquals("  bat", StringUtils.leftPad("bat", 5, ""));
        assertEquals("zzbat", StringUtils.leftPad("bat", 5, "z"));
        assertEquals("yzbat", StringUtils.leftPad("bat", 5, "yz"));
        assertEquals("wxbat", StringUtils.leftPad("bat", 5, "wxyz"));
        assertEquals("yzyzybat", StringUtils.leftPad("bat", 8, "yz"));
        assertEquals("abat", StringUtils.leftPad("bat", 5, "a\uD83D\uDE00"));

        assertEquals("bat  ", StringUtils.rightPad("bat", 5, ""));
        assertEquals("batzz", StringUtils.rightPad("bat", 5, "z"));
        assertEquals("batyz", StringUtils.rightPad("bat", 5, "yz"));
        assertEquals("batwx", StringUtils.rightPad("bat", 5, "wxyz"));
        assertEquals("batyzyzy", StringUtils.rightPad("bat", 8, "yz"));
        assertEquals("bata", StringUtils.rightPad("bat", 5, "a\uD83D\uDE00"));

        // Surrogate pairs for containsAny, containsNone, indexOfAny
        char[] grinning = "\uD83D\uDE00".toCharArray();
        char[] otherEmoji = "\uD83D\uDE01".toCharArray();
        assertTrue(StringUtils.containsAny("a\uD83D\uDE00b", grinning));
        assertFalse(StringUtils.containsAny("a\uD83D\uDE00b", otherEmoji));
        assertFalse(StringUtils.containsNone("a\uD83D\uDE00b", grinning));
        assertTrue(StringUtils.containsNone("a\uD83D\uDE00b", otherEmoji));
        assertEquals(1, StringUtils.indexOfAny("a\uD83D\uDE00b", 0, grinning));
        assertEquals(-1, StringUtils.indexOfAny("a\uD83D\uDE00b", 0, otherEmoji));
    }
}
