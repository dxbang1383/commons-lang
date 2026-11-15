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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

/**
 * AI-generated C2 (Branch Coverage) test suite for {@link SystemProperties}.
 */
@SuppressWarnings("deprecation")
public class SystemPropertiesAiC2Test {

    @Test
    public void testConstructor() {
        assertNotNull(new SystemProperties());
    }

    @Test
    public void testGetPropertyBranches() {
        // null and empty property
        assertNull(SystemProperties.getProperty(null));
        assertNull(SystemProperties.getProperty(""));
        assertEquals("defaultVal", SystemProperties.getProperty(null, "defaultVal"));
        assertEquals("defaultVal", SystemProperties.getProperty("", "defaultVal"));
        assertEquals("defaultVal", SystemProperties.getProperty(null, (Supplier<String>) () -> "defaultVal"));
        assertEquals("defaultVal", SystemProperties.getProperty("", (Supplier<String>) () -> "defaultVal"));
        assertNull(SystemProperties.getProperty(null, (Supplier<String>) null));
        assertNull(SystemProperties.getProperty("", (Supplier<String>) null));

        // Existing property
        assertNotNull(SystemProperties.getProperty("java.home"));
        assertEquals(System.getProperty("java.home"), SystemProperties.getProperty("java.home", "defaultVal"));
        assertEquals(System.getProperty("java.home"), SystemProperties.getProperty("java.home", () -> "defaultVal"));

        // Non-existent property
        final String nonExistentKey = "org.apache.commons.lang3.non.existent.key." + System.currentTimeMillis();
        assertNull(SystemProperties.getProperty(nonExistentKey));
        assertEquals("fallback", SystemProperties.getProperty(nonExistentKey, "fallback"));
        assertEquals("fallback", SystemProperties.getProperty(nonExistentKey, () -> "fallback"));
    }

    @Test
    public void testGetBooleanBranches() {
        final String tempKey = "org.apache.commons.lang3.test.boolean." + System.currentTimeMillis();

        // Property absent, default null
        assertFalse(SystemProperties.getBoolean(tempKey, null));

        // Property absent, default true/false
        assertTrue(SystemProperties.getBoolean(tempKey, () -> true));
        assertFalse(SystemProperties.getBoolean(tempKey, () -> false));

        // With Class<?> clazz
        assertFalse(SystemProperties.getBoolean(SystemPropertiesAiC2Test.class, "absentKey", null));
        assertTrue(SystemProperties.getBoolean(SystemPropertiesAiC2Test.class, "absentKey", () -> true));

        // Property present
        try {
            System.setProperty(tempKey, "true");
            assertTrue(SystemProperties.getBoolean(tempKey, () -> false));

            System.setProperty(tempKey, "false");
            assertFalse(SystemProperties.getBoolean(tempKey, () -> true));

            System.setProperty(tempKey, "notABoolean");
            assertFalse(SystemProperties.getBoolean(tempKey, () -> true));

            final String classKey = "SystemPropertiesAiC2Test.flag";
            System.setProperty(classKey, "true");
            assertTrue(SystemProperties.getBoolean(SystemPropertiesAiC2Test.class, "flag", () -> false));
        } finally {
            System.clearProperty(tempKey);
            System.clearProperty("SystemPropertiesAiC2Test.flag");
        }
    }

    @Test
    public void testGetIntBranches() {
        final String tempKey = "org.apache.commons.lang3.test.int." + System.currentTimeMillis();

        // Property absent, default null
        assertEquals(0, SystemProperties.getInt(tempKey, null));

        // Property absent, default supplier
        assertEquals(42, SystemProperties.getInt(tempKey, () -> 42));

        // With Class<?> clazz
        assertEquals(0, SystemProperties.getInt(SystemPropertiesAiC2Test.class, "absentInt", null));
        assertEquals(100, SystemProperties.getInt(SystemPropertiesAiC2Test.class, "absentInt", () -> 100));

        // Property present
        try {
            System.setProperty(tempKey, "999");
            assertEquals(999, SystemProperties.getInt(tempKey, () -> 42));

            final String classKey = "SystemPropertiesAiC2Test.num";
            System.setProperty(classKey, "777");
            assertEquals(777, SystemProperties.getInt(SystemPropertiesAiC2Test.class, "num", () -> 100));
        } finally {
            System.clearProperty(tempKey);
            System.clearProperty("SystemPropertiesAiC2Test.num");
        }
    }

    @Test
    public void testGetLongBranches() {
        final String tempKey = "org.apache.commons.lang3.test.long." + System.currentTimeMillis();

        // Property absent, default null
        assertEquals(0L, SystemProperties.getLong(tempKey, null));

        // Property absent, default supplier
        assertEquals(12345L, SystemProperties.getLong(tempKey, () -> 12345L));

        // With Class<?> clazz
        assertEquals(0L, SystemProperties.getLong(SystemPropertiesAiC2Test.class, "absentLong", null));
        assertEquals(54321L, SystemProperties.getLong(SystemPropertiesAiC2Test.class, "absentLong", () -> 54321L));

        // Property present
        try {
            System.setProperty(tempKey, "9876543210");
            assertEquals(9876543210L, SystemProperties.getLong(tempKey, () -> 12345L));

            final String classKey = "SystemPropertiesAiC2Test.longNum";
            System.setProperty(classKey, "1122334455");
            assertEquals(1122334455L, SystemProperties.getLong(SystemPropertiesAiC2Test.class, "longNum", () -> 54321L));
        } finally {
            System.clearProperty(tempKey);
            System.clearProperty("SystemPropertiesAiC2Test.longNum");
        }
    }

    @Test
    public void testGetPathBranches() {
        final String tempKey = "org.apache.commons.lang3.test.path." + System.currentTimeMillis();

        // Property absent, default null
        assertNull(SystemProperties.getPath(tempKey, null));

        // Property absent, default supplier
        final Path fallback = Paths.get("fallbackPath");
        assertEquals(fallback, SystemProperties.getPath(tempKey, () -> fallback));

        // Property present
        try {
            System.setProperty(tempKey, "target/testPath");
            assertEquals(Paths.get("target/testPath"), SystemProperties.getPath(tempKey, () -> fallback));
        } finally {
            System.clearProperty(tempKey);
        }
    }

    @Test
    public void testIsPropertySet() {
        assertTrue(SystemProperties.isPropertySet("java.home"));
        assertFalse(SystemProperties.isPropertySet(null));
        assertFalse(SystemProperties.isPropertySet(""));
        assertFalse(SystemProperties.isPropertySet("non.existent.property.xyz." + System.currentTimeMillis()));
    }

    @Test
    public void testAllGetterMethodsViaReflection() throws Exception {
        // Explicitly test overloads taking parameters
        assertNotNull(SystemProperties.getLineSeparator(() -> "\n"));
        assertNotNull(SystemProperties.getJavaSpecificationVersion("1.8"));
        assertNotNull(SystemProperties.getUserName("defaultUser"));

        // Exercise every no-arg public static getter method in SystemProperties
        for (final Method method : SystemProperties.class.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())
                    && Modifier.isStatic(method.getModifiers())
                    && method.getParameterCount() == 0
                    && method.getName().startsWith("get")) {
                method.invoke(null);
            }
        }
    }
}
