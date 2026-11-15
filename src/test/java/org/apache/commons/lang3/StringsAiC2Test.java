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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * C2 (Branch Coverage) test suite for {@link Strings}.
 */
public class StringsAiC2Test {

    // --- Builder ---
    @Test
    public void testBuilder() {
        assertNotNull(Strings.builder());

        // ignoreCase true -> CiStrings
        final Strings ci = Strings.builder().setIgnoreCase(true).setNullIsLess(true).get();
        assertFalse(ci.isCaseSensitive());
        assertTrue(ci.isNullIsLess());

        final Strings ciNullGreater = Strings.builder().setIgnoreCase(true).setNullIsLess(false).get();
        assertFalse(ciNullGreater.isNullIsLess());

        // ignoreCase false -> CsStrings
        final Strings cs = Strings.builder().setIgnoreCase(false).setNullIsLess(true).get();
        assertTrue(cs.isCaseSensitive());
        assertTrue(cs.isNullIsLess());

        final Strings csNullGreater = Strings.builder().setIgnoreCase(false).setNullIsLess(false).get();
        assertFalse(csNullGreater.isNullIsLess());

        // Defaults
        final Strings def = Strings.builder().get();
        assertTrue(def.isCaseSensitive());
    }

    // --- CiStrings ---
    @Test
    public void testCiCompare() {
        final Strings ciLess = Strings.builder().setIgnoreCase(true).setNullIsLess(true).get();
        final Strings ciGreater = Strings.builder().setIgnoreCase(true).setNullIsLess(false).get();

        // s1 == s2
        assertEquals(0, ciLess.compare(null, null));
        final String s = "abc";
        assertEquals(0, ciLess.compare(s, s));

        // s1 == null (nullIsLess: true vs false)
        assertEquals(-1, ciLess.compare(null, "a"));
        assertEquals(1, ciGreater.compare(null, "a"));

        // s2 == null (nullIsLess: true vs false)
        assertEquals(1, ciLess.compare("a", null));
        assertEquals(-1, ciGreater.compare("a", null));

        // compareToIgnoreCase
        assertEquals(0, ciLess.compare("abc", "ABC"));
        assertTrue(ciLess.compare("a", "B") < 0);
        assertTrue(ciLess.compare("B", "a") > 0);
    }

    @Test
    public void testCiContains() {
        assertFalse(Strings.CI.contains(null, "a"));
        assertFalse(Strings.CI.contains("a", null));
        assertFalse(Strings.CI.contains(null, null));

        // regionMatches match
        assertTrue(Strings.CI.contains("aBcDe", "cd"));
        assertTrue(Strings.CI.contains("abc", ""));

        // loop exhausts without match
        assertFalse(Strings.CI.contains("aBcDe", "xyz"));
        assertFalse(Strings.CI.contains("a", "longerString"));
    }

    @Test
    public void testCiEqualsCharSequence() {
        // cs1 == cs2
        assertTrue(Strings.CI.equals((CharSequence) null, (CharSequence) null));
        final CharSequence cs = new StringBuilder("abc");
        assertTrue(Strings.CI.equals(cs, cs));

        // null checks with explicit CharSequence
        assertFalse(Strings.CI.equals((CharSequence) null, (CharSequence) "abc"));
        assertFalse(Strings.CI.equals((CharSequence) "abc", (CharSequence) null));

        // length difference with explicit CharSequence
        assertFalse(Strings.CI.equals((CharSequence) "abc", (CharSequence) "abcd"));
        assertFalse(Strings.CI.equals((CharSequence) new StringBuilder("abc"), (CharSequence) new StringBuilder("abcd")));

        // regionMatches match & mismatch
        assertTrue(Strings.CI.equals((CharSequence) new StringBuilder("abc"), (CharSequence) "ABC"));
        assertTrue(Strings.CI.equals((CharSequence) "abc", (CharSequence) "ABC"));
        assertFalse(Strings.CI.equals((CharSequence) new StringBuilder("abc"), (CharSequence) "ABD"));
        assertFalse(Strings.CI.equals((CharSequence) "abc", (CharSequence) "xyz"));
    }

    @Test
    public void testCiEqualsString() {
        // s1 == null -> s2 == null
        assertTrue(Strings.CI.equals((String) null, (String) null));
        assertFalse(Strings.CI.equals((String) null, "abc"));

        // s1 != null -> equalsIgnoreCase
        assertTrue(Strings.CI.equals("abc", "ABC"));
        assertFalse(Strings.CI.equals("abc", "def"));
        assertFalse(Strings.CI.equals("abc", (String) null));
    }

    @Test
    public void testCiIndexOf() {
        // null inputs
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.indexOf(null, "a", 0));
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.indexOf("a", null, 0));

        // startPos < 0
        assertEquals(1, Strings.CI.indexOf("aBc", "B", -5));

        // startPos >= endLimit
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.indexOf("abc", "b", 10));

        // searchStr.length() == 0
        assertEquals(2, Strings.CI.indexOf("abc", "", 2));

        // loop match
        assertEquals(2, Strings.CI.indexOf("aBcDe", "CD", 0));

        // loop exhausted
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.indexOf("abc", "z", 0));
    }

    @Test
    public void testCiLastIndexOf() {
        // null inputs
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.lastIndexOf(null, "a", 0));
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.lastIndexOf("a", null, 0));

        // startPos > strLength - searchStrLength
        assertEquals(2, Strings.CI.lastIndexOf("aBcDe", "C", 10));

        // startPos < 0
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.lastIndexOf("abc", "a", -1));

        // searchStrLength == 0
        assertEquals(3, Strings.CI.lastIndexOf("abc", "", 3));

        // loop match
        assertEquals(5, Strings.CI.lastIndexOf("aBcDaBc", "BC", 6));

        // loop exhausted
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CI.lastIndexOf("abc", "z", 2));
    }

    // --- CsStrings ---
    @Test
    public void testCsCompare() {
        final Strings csLess = Strings.builder().setIgnoreCase(false).setNullIsLess(true).get();
        final Strings csGreater = Strings.builder().setIgnoreCase(false).setNullIsLess(false).get();

        // s1 == s2
        assertEquals(0, csLess.compare(null, null));
        final String s = "abc";
        assertEquals(0, csLess.compare(s, s));

        // s1 == null
        assertEquals(-1, csLess.compare(null, "a"));
        assertEquals(1, csGreater.compare(null, "a"));

        // s2 == null
        assertEquals(1, csLess.compare("a", null));
        assertEquals(-1, csGreater.compare("a", null));

        // s1.compareTo(s2)
        assertEquals(0, csLess.compare("abc", "abc"));
        assertTrue(csLess.compare("a", "b") < 0);
        assertTrue(csLess.compare("b", "a") > 0);
    }

    @Test
    public void testCsContains() {
        assertTrue(Strings.CS.contains("abc", "b"));
        assertFalse(Strings.CS.contains("abc", "B"));
        assertFalse(Strings.CS.contains("abc", "z"));
        assertFalse(Strings.CS.contains(null, "a"));
    }

    @Test
    public void testCsEqualsCharSequence() {
        // cs1 == cs2
        assertTrue(Strings.CS.equals((CharSequence) null, (CharSequence) null));
        final CharSequence cs = new StringBuilder("abc");
        assertTrue(Strings.CS.equals(cs, cs));

        // null checks with explicit CharSequence
        assertFalse(Strings.CS.equals((CharSequence) null, (CharSequence) "abc"));
        assertFalse(Strings.CS.equals((CharSequence) "abc", (CharSequence) null));

        // length mismatch with explicit CharSequence
        assertFalse(Strings.CS.equals((CharSequence) "abc", (CharSequence) "abcd"));
        assertFalse(Strings.CS.equals((CharSequence) new StringBuilder("abc"), (CharSequence) new StringBuilder("abcd")));

        // String vs String
        assertTrue(Strings.CS.equals((CharSequence) "abc", (CharSequence) "abc"));
        assertFalse(Strings.CS.equals((CharSequence) "abc", (CharSequence) "ABC"));

        // String vs non-String (cs1 is String, cs2 is NOT String)
        assertTrue(Strings.CS.equals((CharSequence) "abc", (CharSequence) new StringBuilder("abc")));
        assertFalse(Strings.CS.equals((CharSequence) "abc", (CharSequence) new StringBuilder("xyz")));

        // non-String vs String (cs1 is NOT String, cs2 IS String)
        assertTrue(Strings.CS.equals((CharSequence) new StringBuilder("abc"), (CharSequence) "abc"));
        assertFalse(Strings.CS.equals((CharSequence) new StringBuilder("abc"), (CharSequence) "xyz"));

        // Non-string vs Non-string CharSequence step-wise loop
        assertTrue(Strings.CS.equals((CharSequence) new StringBuilder("abc"), (CharSequence) new StringBuffer("abc")));
        assertFalse(Strings.CS.equals((CharSequence) new StringBuilder("abc"), (CharSequence) new StringBuffer("abd")));
    }

    @Test
    public void testCsEqualsString() {
        assertTrue(Strings.CS.equals((String) null, (String) null));
        assertFalse(Strings.CS.equals((String) null, "abc"));
        assertFalse(Strings.CS.equals("abc", (String) null));
        assertTrue(Strings.CS.equals("abc", "abc"));
        assertFalse(Strings.CS.equals("abc", "ABC"));
    }

    @Test
    public void testCsIndexOfAndLastIndexOf() {
        assertEquals(1, Strings.CS.indexOf("abc", "b", 0));
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CS.indexOf("abc", "B", 0));

        assertEquals(3, Strings.CS.lastIndexOf("abcb", "b", 3));
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CS.lastIndexOf("abcb", "B", 3));
    }

    // --- Base class Strings methods ---
    @Test
    public void testContainsAny() {
        // cs empty or null
        assertFalse(Strings.CS.containsAny(null, "a"));
        assertFalse(Strings.CS.containsAny("", "a"));

        // searchCharSequences empty or null
        assertFalse(Strings.CS.containsAny("abc", (CharSequence[]) null));
        assertFalse(Strings.CS.containsAny("abc"));

        // loop matches
        assertTrue(Strings.CS.containsAny("hello", "foo", "ell"));

        // loop exhausted
        assertFalse(Strings.CS.containsAny("hello", "foo", "bar"));
    }

    @Test
    public void testInitialCapacity() {
        // max < 0
        final int capNegativeMax = Strings.initialCapacity(10, 2, 5, -1);
        assertTrue(capNegativeMax >= 10);

        // max >= 0 (e.g. max=2, max=100)
        final int capSmallMax = Strings.initialCapacity(10, 2, 5, 2);
        assertTrue(capSmallMax >= 10);

        final int capLargeMax = Strings.initialCapacity(10, 2, 5, 100);
        assertTrue(capLargeMax >= 10);

        // replacementLen <= searchLen
        assertEquals(10, Strings.initialCapacity(10, 5, 2, 5));

        // Overflow clamping to SAFE_MAX_ARRAY_LENGTH
        assertEquals(ArrayUtils.SAFE_MAX_ARRAY_LENGTH,
                Strings.initialCapacity(Integer.MAX_VALUE - 100, 1, 1000, 50));
    }

    @Test
    public void testAppendIfMissing() {
        // str == null
        assertNull(Strings.CS.appendIfMissing(null, "xyz"));

        // suffix is empty or null
        assertEquals("abc", Strings.CS.appendIfMissing("abc", null));
        assertEquals("abc", Strings.CS.appendIfMissing("abc", ""));

        // str endsWith suffix
        assertEquals("abcxyz", Strings.CS.appendIfMissing("abcxyz", "xyz"));

        // suffixes not empty, one matches
        assertEquals("abcmno", Strings.CS.appendIfMissing("abcmno", "xyz", "mno"));

        // suffixes not empty, none matches
        assertEquals("abcxyz", Strings.CS.appendIfMissing("abc", "xyz", "mno", "pqr"));

        // suffixes is empty/null
        assertEquals("abcxyz", Strings.CS.appendIfMissing("abc", "xyz", (CharSequence[]) null));
        assertEquals("abcxyz", Strings.CS.appendIfMissing("abc", "xyz"));
    }

    @Test
    public void testEndsWith() {
        // str == null || suffix == null
        assertTrue(Strings.CS.endsWith(null, null));
        assertFalse(Strings.CS.endsWith(null, "def"));
        assertFalse(Strings.CS.endsWith("abc", null));

        // sufLen > str.length()
        assertFalse(Strings.CS.endsWith("a", "abc"));

        // regionMatches match vs mismatch
        assertTrue(Strings.CS.endsWith("abcdef", "def"));
        assertFalse(Strings.CS.endsWith("abcdef", "xyz"));
        assertFalse(Strings.CS.endsWith("ABCDEF", "def"));

        // CI mode
        assertTrue(Strings.CI.endsWith("ABCDEF", "def"));
    }

    @Test
    public void testEndsWithAny() {
        // sequence empty/null
        assertFalse(Strings.CS.endsWithAny(null, "a"));
        assertFalse(Strings.CS.endsWithAny("", "a"));

        // searchStrings empty/null
        assertFalse(Strings.CS.endsWithAny("abc", (CharSequence[]) null));
        assertFalse(Strings.CS.endsWithAny("abc"));

        // loop match
        assertTrue(Strings.CS.endsWithAny("abcdef", "xyz", "def"));

        // loop exhausted
        assertFalse(Strings.CS.endsWithAny("abcdef", "xyz", "mno"));
    }

    @Test
    public void testEqualsAny() {
        // searchStrings empty/null
        assertFalse(Strings.CS.equalsAny("abc", (CharSequence[]) null));
        assertFalse(Strings.CS.equalsAny("abc"));

        // loop match
        assertTrue(Strings.CS.equalsAny("abc", "xyz", "abc"));

        // loop exhausted
        assertFalse(Strings.CS.equalsAny("abc", "xyz", "mno"));
    }

    @Test
    public void testIndexOfOverload() {
        assertEquals(1, Strings.CS.indexOf("abc", "b"));
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CS.indexOf("abc", "z"));
    }

    @Test
    public void testLastIndexOfOverload() {
        assertEquals(StringUtils.INDEX_NOT_FOUND, Strings.CS.lastIndexOf(null, "a"));
        assertEquals(2, Strings.CS.lastIndexOf("aba", "a"));
    }

    @Test
    public void testPrependIfMissing() {
        // str == null
        assertNull(Strings.CS.prependIfMissing(null, "xyz"));

        // prefix empty or null
        assertEquals("abc", Strings.CS.prependIfMissing("abc", null));
        assertEquals("abc", Strings.CS.prependIfMissing("abc", ""));

        // startsWith prefix
        assertEquals("xyzabc", Strings.CS.prependIfMissing("xyzabc", "xyz"));

        // prefixes not empty, one matches
        assertEquals("mnoabc", Strings.CS.prependIfMissing("mnoabc", "xyz", "mno"));

        // prefixes not empty, none matches
        assertEquals("xyzabc", Strings.CS.prependIfMissing("abc", "xyz", "mno", "pqr"));

        // prefixes is empty
        assertEquals("xyzabc", Strings.CS.prependIfMissing("abc", "xyz", (CharSequence[]) null));
        assertEquals("xyzabc", Strings.CS.prependIfMissing("abc", "xyz"));
    }

    @Test
    public void testRemove() {
        assertNull(Strings.CS.remove(null, "a"));
        assertEquals("qd", Strings.CS.remove("queued", "ue"));
        assertEquals("qd", Strings.CI.remove("quEUed", "ue"));
    }

    @Test
    public void testRemoveEnd() {
        // str or remove empty/null
        assertNull(Strings.CS.removeEnd(null, "a"));
        assertEquals("", Strings.CS.removeEnd("", "a"));
        assertEquals("abc", Strings.CS.removeEnd("abc", null));
        assertEquals("abc", Strings.CS.removeEnd("abc", ""));

        // endsWith true
        assertEquals("domain", Strings.CS.removeEnd("domain.com", ".com"));
        assertEquals("domain", Strings.CI.removeEnd("domain.COM", ".com"));

        // endsWith false
        assertEquals("domain.com", Strings.CS.removeEnd("domain.com", ".org"));
    }

    @Test
    public void testRemoveStart() {
        // str == null
        assertNull(Strings.CS.removeStart(null, "a"));

        // startsWith true
        assertEquals("domain.com", Strings.CS.removeStart("www.domain.com", "www."));
        assertEquals("domain.com", Strings.CI.removeStart("WWW.domain.com", "www."));

        // startsWith false
        assertEquals("domain.com", Strings.CS.removeStart("domain.com", "www."));
    }

    @Test
    public void testReplace() {
        // simple 3-arg replace
        assertNull(Strings.CS.replace(null, "a", "b"));
        assertEquals("zbz", Strings.CS.replace("aba", "a", "z"));

        // 4-arg replace: boundary checks
        assertEquals("", Strings.CS.replace("", "a", "b", -1));
        assertEquals("abc", Strings.CS.replace("abc", null, "b", -1));
        assertEquals("abc", Strings.CS.replace("abc", "", "b", -1));
        assertEquals("abc", Strings.CS.replace("abc", "a", null, -1));
        assertEquals("abc", Strings.CS.replace("abc", "a", "b", 0));

        // end == INDEX_NOT_FOUND
        assertEquals("abc", Strings.CS.replace("abc", "z", "r", -1));

        // max reached: max=1 and max=2
        assertEquals("zbaa", Strings.CS.replace("abaa", "a", "z", 1));
        assertEquals("zbza", Strings.CS.replace("abaa", "a", "z", 2));

        // max=-1 (unlimited)
        assertEquals("zbzz", Strings.CS.replace("abaa", "a", "z", -1));
    }

    @Test
    public void testReplaceOnce() {
        assertNull(Strings.CS.replaceOnce(null, "a", "z"));
        assertEquals("zba", Strings.CS.replaceOnce("aba", "a", "z"));
        assertEquals("zba", Strings.CI.replaceOnce("Aba", "a", "z"));
    }

    @Test
    public void testStartsWith() {
        // str == null || prefix == null
        assertTrue(Strings.CS.startsWith(null, null));
        assertFalse(Strings.CS.startsWith(null, "abc"));
        assertFalse(Strings.CS.startsWith("abc", null));

        // preLen > str.length()
        assertFalse(Strings.CS.startsWith("a", "abc"));

        // regionMatches match vs mismatch
        assertTrue(Strings.CS.startsWith("abcdef", "abc"));
        assertFalse(Strings.CS.startsWith("abcdef", "xyz"));
        assertFalse(Strings.CS.startsWith("ABCDEF", "abc"));

        // CI mode
        assertTrue(Strings.CI.startsWith("ABCDEF", "abc"));
    }

    @Test
    public void testStartsWithAny() {
        // sequence empty/null
        assertFalse(Strings.CS.startsWithAny(null, "a"));
        assertFalse(Strings.CS.startsWithAny("", "a"));

        // searchStrings empty/null
        assertFalse(Strings.CS.startsWithAny("abc", (CharSequence[]) null));
        assertFalse(Strings.CS.startsWithAny("abc"));

        // loop match
        assertTrue(Strings.CS.startsWithAny("abcdef", "xyz", "abc"));

        // loop exhausted
        assertFalse(Strings.CS.startsWithAny("abcdef", "xyz", "mno"));
    }
}
