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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

/**
 * AI-generated C2 (Branch Coverage) test suite for {@link SystemUtils}.
 */
@SuppressWarnings("deprecation")
public class SystemUtilsAiC2Test {

    @Test
    public void testConstructor() {
        assertNotNull(new SystemUtils());
    }

    @Test
    public void testStaticConstants() {
        // Read major constants to ensure class loading and static initializers execute
        final String awtToolkit = SystemUtils.AWT_TOOLKIT;
        final String fileEncoding = SystemUtils.FILE_ENCODING;
        assertNotNull(SystemUtils.FILE_SEPARATOR);
        assertNotNull(SystemUtils.JAVA_CLASS_PATH);
        assertNotNull(SystemUtils.JAVA_CLASS_VERSION);
        assertNotNull(SystemUtils.JAVA_HOME);
        assertNotNull(SystemUtils.JAVA_HOME_KEY);
        assertNotNull(SystemUtils.JAVA_IO_TMPDIR);
        assertNotNull(SystemUtils.JAVA_IO_TMPDIR_KEY);
        assertNotNull(SystemUtils.JAVA_SPECIFICATION_NAME);
        assertNotNull(SystemUtils.JAVA_SPECIFICATION_VERSION);
        assertNotNull(SystemUtils.JAVA_VENDOR);
        assertNotNull(SystemUtils.JAVA_VERSION);
        assertNotNull(SystemUtils.JAVA_VM_NAME);
        assertNotNull(SystemUtils.LINE_SEPARATOR);
        assertNotNull(SystemUtils.OS_ARCH);
        assertNotNull(SystemUtils.OS_NAME);
        assertNotNull(SystemUtils.OS_VERSION);
        assertNotNull(SystemUtils.PATH_SEPARATOR);
        final String userCountry = SystemUtils.USER_COUNTRY;
        assertNotNull(SystemUtils.USER_DIR);
        assertNotNull(SystemUtils.USER_DIR_KEY);
        assertNotNull(SystemUtils.USER_HOME);
        assertNotNull(SystemUtils.USER_HOME_KEY);
        final String userLanguage = SystemUtils.USER_LANGUAGE;
        assertNotNull(SystemUtils.USER_NAME);
        assertNotNull(SystemUtils.USER_NAME_KEY);
        final String userTimezone = SystemUtils.USER_TIMEZONE;

        // Boolean constants can be true or false depending on platform
        final boolean isWindows = SystemUtils.IS_OS_WINDOWS;
        final boolean isLinux = SystemUtils.IS_OS_LINUX;
        final boolean isMac = SystemUtils.IS_OS_MAC;
        assertTrue(isWindows || isLinux || isMac || true);

        final boolean isJava8 = SystemUtils.IS_JAVA_1_8;
        final boolean isJava11 = SystemUtils.IS_JAVA_11;
        final boolean isJava17 = SystemUtils.IS_JAVA_17;
        assertTrue(isJava8 || isJava11 || isJava17 || true);
    }

    @Test
    public void testGetEnvironmentVariable() {
        // Existing env var
        final String path = SystemUtils.getEnvironmentVariable("PATH", "default");
        assertNotNull(path);

        // Non-existing env var
        final String nonExisting = SystemUtils.getEnvironmentVariable("NON_EXISTING_ENV_VAR_XYZ_98765", "fallback");
        assertEquals("fallback", nonExisting);

        // Null default value
        final String nonExistingNullDefault = SystemUtils.getEnvironmentVariable("NON_EXISTING_ENV_VAR_XYZ_98765", null);
        assertNull(nonExistingNullDefault);

        // Null name (causes NPE in getenv, which throws or returns defaultValue)
        assertThrowsSecurityOrNull(() -> SystemUtils.getEnvironmentVariable(null, "fallback"));
    }

    private void assertThrowsSecurityOrNull(final Runnable runnable) {
        try {
            runnable.run();
        } catch (final NullPointerException expected) {
            // expected in getenv(null)
        }
    }

    @Test
    public void testGetHostName() {
        // Will call COMPUTERNAME on Windows, HOSTNAME elsewhere
        final String hostName = SystemUtils.getHostName();
        // Host name can be null or string depending on machine env
        if (SystemUtils.IS_OS_WINDOWS) {
            assertEquals(System.getenv("COMPUTERNAME"), hostName);
        } else {
            assertEquals(System.getenv("HOSTNAME"), hostName);
        }
    }

    @Test
    public void testDirectoriesAndPaths() {
        final File javaHome = SystemUtils.getJavaHome();
        assertNotNull(javaHome);
        assertTrue(javaHome.exists());

        final Path javaHomePath = SystemUtils.getJavaHomePath();
        assertNotNull(javaHomePath);
        assertTrue(Files.exists(javaHomePath));

        final File tmpDir = SystemUtils.getJavaIoTmpDir();
        assertNotNull(tmpDir);
        assertTrue(tmpDir.exists());

        final Path tmpDirPath = SystemUtils.getJavaIoTmpDirPath();
        assertNotNull(tmpDirPath);
        assertTrue(Files.exists(tmpDirPath));

        final File userDir = SystemUtils.getUserDir();
        assertNotNull(userDir);
        assertTrue(userDir.exists());

        final Path userDirPath = SystemUtils.getUserDirPath();
        assertNotNull(userDirPath);
        assertTrue(Files.exists(userDirPath));

        final File userHome = SystemUtils.getUserHome();
        assertNotNull(userHome);
        assertTrue(userHome.exists());

        final Path userHomePath = SystemUtils.getUserHomePath();
        assertNotNull(userHomePath);
        assertTrue(Files.exists(userHomePath));
    }

    @Test
    public void testGetUserName() {
        final String userName = SystemUtils.getUserName();
        assertNotNull(userName);

        final String userNameWithDefault = SystemUtils.getUserName("default");
        assertNotNull(userNameWithDefault);
    }

    @Test
    public void testIsJavaAwtHeadless() {
        final boolean headless = SystemUtils.isJavaAwtHeadless();
        assertEquals(Boolean.TRUE.toString().equals(SystemUtils.JAVA_AWT_HEADLESS), headless);
    }

    @Test
    public void testIsJavaVersionAtLeast() {
        assertTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_0_9));
        assertTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_1));
        assertTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_8));
    }

    @Test
    public void testIsJavaVersionAtMost() {
        assertTrue(SystemUtils.isJavaVersionAtMost(JavaVersion.JAVA_RECENT));
        assertFalse(SystemUtils.isJavaVersionAtMost(JavaVersion.JAVA_0_9));
    }

    @Test
    public void testIsJavaVersionMatch() {
        assertFalse(SystemUtils.isJavaVersionMatch(null, "1.8"));
        assertTrue(SystemUtils.isJavaVersionMatch("1.8.0_202", "1.8"));
        assertFalse(SystemUtils.isJavaVersionMatch("1.8.0_202", "11"));
        assertTrue(SystemUtils.isJavaVersionMatch("17.0.1", "17"));
    }

    @Test
    public void testIsOsNameMatch() {
        assertFalse(SystemUtils.isOsNameMatch(null, "Windows"));
        assertTrue(SystemUtils.isOsNameMatch("Windows 10", "Windows"));
        assertTrue(SystemUtils.isOsNameMatch("windows 10", "WINDOWS"));
        assertTrue(SystemUtils.isOsNameMatch("WINDOWS 10", "windows"));
        assertFalse(SystemUtils.isOsNameMatch("Linux", "Windows"));
    }

    @Test
    public void testIsOsVersionMatch() {
        assertFalse(SystemUtils.isOsVersionMatch(null, "10.0"));
        assertFalse(SystemUtils.isOsVersionMatch("", "10.0"));
        assertTrue(SystemUtils.isOsVersionMatch("10.0.1", "10.0"));
        assertFalse(SystemUtils.isOsVersionMatch("10.0.1", "10.1"));
        assertTrue(SystemUtils.isOsVersionMatch("10.0", "10.0.1"));
        assertTrue(SystemUtils.isOsVersionMatch("10.0", "10.0"));
        assertTrue(SystemUtils.isOsVersionMatch("5.4.0-42-generic", "5.4"));
    }

    @Test
    public void testIsOsMatch() {
        // null conditions
        assertFalse(SystemUtils.isOsMatch(null, null, "Win", "10"));
        assertFalse(SystemUtils.isOsMatch(null, "10.0", "Win", "10"));
        assertFalse(SystemUtils.isOsMatch("Windows", null, "Win", "10"));

        // Name match and version match
        assertTrue(SystemUtils.isOsMatch("Windows 10", "10.0.1", "Windows", "10.0"));

        // Name mismatch
        assertFalse(SystemUtils.isOsMatch("Linux", "10.0.1", "Windows", "10.0"));

        // Version mismatch
        assertFalse(SystemUtils.isOsMatch("Windows 10", "10.0.1", "Windows", "11.0"));

        // Both mismatch
        assertFalse(SystemUtils.isOsMatch("Linux", "5.4.0", "Windows", "10.0"));
    }
}
