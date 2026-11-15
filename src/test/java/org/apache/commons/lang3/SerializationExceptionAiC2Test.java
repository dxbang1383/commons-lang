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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

/**
 * AI-generated C2 (Branch Coverage) test suite for {@link SerializationException}.
 */
public class SerializationExceptionAiC2Test {

    @Test
    public void testConstructorNoArg() {
        final SerializationException ex = new SerializationException();
        assertNotNull(ex);
        assertNull(ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void testConstructorWithMessage() {
        final String msg = "error message";
        final SerializationException ex = new SerializationException(msg);
        assertNotNull(ex);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());

        final SerializationException nullMsgEx = new SerializationException((String) null);
        assertNull(nullMsgEx.getMessage());
    }

    @Test
    public void testConstructorWithMessageAndCause() {
        final String msg = "error message";
        final Throwable cause = new IllegalArgumentException("root cause");
        final SerializationException ex = new SerializationException(msg, cause);
        assertNotNull(ex);
        assertEquals(msg, ex.getMessage());
        assertSame(cause, ex.getCause());

        final SerializationException nullEx = new SerializationException(null, null);
        assertNull(nullEx.getMessage());
        assertNull(nullEx.getCause());
    }

    @Test
    public void testConstructorWithCause() {
        final Throwable cause = new RuntimeException("underlying failure");
        final SerializationException ex = new SerializationException(cause);
        assertNotNull(ex);
        assertSame(cause, ex.getCause());
        assertTrue(ex.getMessage().contains("underlying failure"));

        final SerializationException nullCauseEx = new SerializationException((Throwable) null);
        assertNull(nullCauseEx.getCause());
    }

    @Test
    public void testThrowAndCatch() {
        assertThrows(SerializationException.class, () -> {
            throw new SerializationException("thrown exception");
        });
    }

    @Test
    public void testSerialization() throws Exception {
        final SerializationException original = new SerializationException("serialize test", new IllegalStateException("cause"));

        final byte[] bytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
            oos.flush();
            bytes = baos.toByteArray();
        }

        assertTrue(bytes.length > 0);

        final SerializationException deserialized;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserialized = (SerializationException) ois.readObject();
        }

        assertNotNull(deserialized);
        assertEquals(original.getMessage(), deserialized.getMessage());
        assertNotNull(deserialized.getCause());
        assertEquals("cause", deserialized.getCause().getMessage());
    }
}
