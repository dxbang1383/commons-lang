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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

/**
 * C2 (Branch Coverage) test suite for {@link StringEscapeUtils}.
 */
@SuppressWarnings("deprecation")
public class StringEscapeUtilsAiC2Test {

    @Test
    public void testConstructor() {
        assertNotNull(new StringEscapeUtils());
    }

    // --- CsvEscaper CFG branches ---
    @Test
    public void testCsvEscaperIndexNotZero() {
        // True branch: index != 0 -> throws IllegalStateException
        assertThrows(IllegalStateException.class, () ->
                StringEscapeUtils.ESCAPE_CSV.translate("foo", 1, new StringWriter()));
        assertThrows(IllegalStateException.class, () ->
                StringEscapeUtils.ESCAPE_CSV.translate("foo", 2, new StringWriter()));
    }

    @Test
    public void testCsvEscaperContainsNone() {
        // False branch of containsNone: StringUtils.containsNone(...) is true
        // No comma, no double-quote, no CR, no LF
        assertEquals("simpleText", StringEscapeUtils.escapeCsv("simpleText"));
        assertEquals("", StringEscapeUtils.escapeCsv(""));
        assertNull(StringEscapeUtils.escapeCsv(null));
    }

    @Test
    public void testCsvEscaperContainsSearchChars() {
        // False branch of containsNone: input contains CSV_SEARCH_CHARS
        // 1. Contains comma
        assertEquals("\"hello,world\"", StringEscapeUtils.escapeCsv("hello,world"));

        // 2. Contains quote (escaped to double double-quote)
        assertEquals("\"he said \"\"hi\"\"\"", StringEscapeUtils.escapeCsv("he said \"hi\""));

        // 3. Contains CR
        assertEquals("\"line1\rline2\"", StringEscapeUtils.escapeCsv("line1\rline2"));

        // 4. Contains LF
        assertEquals("\"line1\nline2\"", StringEscapeUtils.escapeCsv("line1\nline2"));

        // 5. Mixed comma and quote
        assertEquals("\"\"\"a\"\",b\"", StringEscapeUtils.escapeCsv("\"a\",b"));
    }

    // --- CsvUnescaper CFG branches ---
    @Test
    public void testCsvUnescaperIndexNotZero() {
        // True branch: index != 0 -> throws IllegalStateException
        assertThrows(IllegalStateException.class, () ->
                StringEscapeUtils.UNESCAPE_CSV.translate("foo", 1, new StringWriter()));
    }

    @Test
    public void testCsvUnescaperShortOrUnquoted() {
        // Branch: input.length() < 2 || input.charAt(0) != CSV_QUOTE || input.charAt(last) != CSV_QUOTE
        // 1. input == null
        assertNull(StringEscapeUtils.unescapeCsv(null));

        // 2. input.length() < 2 (length 0 and length 1)
        assertEquals("", StringEscapeUtils.unescapeCsv(""));
        assertEquals("a", StringEscapeUtils.unescapeCsv("a"));
        assertEquals("\"", StringEscapeUtils.unescapeCsv("\""));

        // 3. input.charAt(0) != CSV_QUOTE
        assertEquals("abc", StringEscapeUtils.unescapeCsv("abc"));
        assertEquals("abc\"", StringEscapeUtils.unescapeCsv("abc\""));

        // 4. input.charAt(last) != CSV_QUOTE
        assertEquals("\"abc", StringEscapeUtils.unescapeCsv("\"abc"));
    }

    @Test
    public void testCsvUnescaperQuotedWithoutSearchChars() {
        // input starts & ends with CSV_QUOTE, but quoteless contains NONE of CSV_SEARCH_CHARS
        // StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS) is false -> out.write(input.toString())
        assertEquals("\"simple\"", StringEscapeUtils.unescapeCsv("\"simple\""));
    }

    @Test
    public void testCsvUnescaperQuotedWithSearchChars() {
        // input starts & ends with CSV_QUOTE, and quoteless contains CSV_SEARCH_CHARS
        // StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS) is true -> unescape "" to "
        // 1. Contains comma
        assertEquals("hello,world", StringEscapeUtils.unescapeCsv("\"hello,world\""));

        // 2. Contains double quotes
        assertEquals("he said \"hi\"", StringEscapeUtils.unescapeCsv("\"he said \"\"hi\"\"\""));

        // 3. Contains CR
        assertEquals("line1\rline2", StringEscapeUtils.unescapeCsv("\"line1\rline2\""));

        // 4. Contains LF
        assertEquals("line1\nline2", StringEscapeUtils.unescapeCsv("\"line1\nline2\""));

        // 5. Empty quoted string (quoteless is "")
        assertEquals("\"\"", StringEscapeUtils.unescapeCsv("\"\""));
    }

    // --- Helper methods / Translators in StringEscapeUtils ---

    @Test
    public void testEscapeJava() {
        assertNull(StringEscapeUtils.escapeJava(null));
        assertEquals("Hello \\\"World\\\"\\t\\n\\r\\\\",
                StringEscapeUtils.escapeJava("Hello \"World\"\t\n\r\\"));
    }

    @Test
    public void testUnescapeJava() {
        assertNull(StringEscapeUtils.unescapeJava(null));
        assertEquals("Hello \"World\"\t\n\r\\ ' A",
                StringEscapeUtils.unescapeJava("Hello \\\"World\\\"\\t\\n\\r\\\\ \\' \\u0041"));
    }

    @Test
    public void testEscapeEcmaScript() {
        assertNull(StringEscapeUtils.escapeEcmaScript(null));
        assertEquals("He didn\\'t say, \\\"Stop!\\\" \\/ \\` \\${val}",
                StringEscapeUtils.escapeEcmaScript("He didn't say, \"Stop!\" / ` ${val}"));
    }

    @Test
    public void testUnescapeEcmaScript() {
        assertNull(StringEscapeUtils.unescapeEcmaScript(null));
        assertEquals("He didn't say, \"Stop!\"",
                StringEscapeUtils.unescapeEcmaScript("He didn\\'t say, \\\"Stop!\\\""));
    }

    @Test
    public void testEscapeJson() {
        assertNull(StringEscapeUtils.escapeJson(null));
        assertEquals("{\\\"key\\\": \\\"value\\\" \\/ \\t}",
                StringEscapeUtils.escapeJson("{\"key\": \"value\" / \t}"));
    }

    @Test
    public void testUnescapeJson() {
        assertNull(StringEscapeUtils.unescapeJson(null));
        assertEquals("{\"key\": \"value\"}",
                StringEscapeUtils.unescapeJson("{\\\"key\\\": \\\"value\\\"}"));
    }

    @Test
    public void testEscapeHtml3() {
        assertNull(StringEscapeUtils.escapeHtml3(null));
        assertEquals("&quot;bread&quot; &amp; &#39;butter&#39; &lt;&gt;",
                StringEscapeUtils.escapeHtml3("\"bread\" & 'butter' <>"));
    }

    @Test
    public void testUnescapeHtml3() {
        assertNull(StringEscapeUtils.unescapeHtml3(null));
        assertEquals("\"bread\" & 'butter' <>",
                StringEscapeUtils.unescapeHtml3("&quot;bread&quot; &amp; &#39;butter&#39; &lt;&gt;"));
    }

    @Test
    public void testEscapeHtml4() {
        assertNull(StringEscapeUtils.escapeHtml4(null));
        assertEquals("&quot;bread&quot; &amp; &lt;Fran&ccedil;ais&gt;",
                StringEscapeUtils.escapeHtml4("\"bread\" & <Fran\u00E7ais>"));
    }

    @Test
    public void testUnescapeHtml4() {
        assertNull(StringEscapeUtils.unescapeHtml4(null));
        assertEquals("<Français> & \"bread\"",
                StringEscapeUtils.unescapeHtml4("&lt;Fran&ccedil;ais&gt; &amp; &quot;bread&quot;"));
    }

    @Test
    public void testEscapeXml() {
        assertNull(StringEscapeUtils.escapeXml(null));
        assertEquals("&quot;bread&quot; &amp; &apos;butter&apos; &lt;&gt;",
                StringEscapeUtils.escapeXml("\"bread\" & 'butter' <>"));
    }

    @Test
    public void testUnescapeXml() {
        assertNull(StringEscapeUtils.unescapeXml(null));
        assertEquals("\"bread\" & 'butter' <>",
                StringEscapeUtils.unescapeXml("&quot;bread&quot; &amp; &apos;butter&apos; &lt;&gt;"));
    }

    @Test
    public void testEscapeXml10() {
        assertNull(StringEscapeUtils.escapeXml10(null));
        // Control char \u0000 is removed in XML 1.0
        assertEquals("&lt;test&gt;", StringEscapeUtils.escapeXml10("\u0000<test>"));
    }

    @Test
    public void testEscapeXml11() {
        assertNull(StringEscapeUtils.escapeXml11(null));
        // Control char \u000b is converted to &#11; in XML 1.1
        assertEquals("&#11;&lt;test&gt;", StringEscapeUtils.escapeXml11("\u000b<test>"));
    }
}
