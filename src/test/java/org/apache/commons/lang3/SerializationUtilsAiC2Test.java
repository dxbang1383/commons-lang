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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang3.SerializationUtils.ClassLoaderAwareObjectInputStream;
import org.junit.jupiter.api.Test;

/**
 * C2 (Branch Coverage) test suite for {@link SerializationUtils}.
 */
public class SerializationUtilsAiC2Test {

    static class ThrowingWriteSerializable implements Serializable {
        private static final long serialVersionUID = 1L;

        private void writeObject(final ObjectOutputStream out) throws IOException {
            throw new IOException("Simulated writeObject error");
        }
    }

    static class ThrowingReadSerializable implements Serializable {
        private static final long serialVersionUID = 1L;

        private void readObject(final ObjectInputStream in) throws IOException {
            throw new IOException("Simulated readObject error");
        }
    }

    static class ClassNotFoundReadSerializable implements Serializable {
        private static final long serialVersionUID = 1L;

        private void readObject(final ObjectInputStream in) throws ClassNotFoundException {
            throw new ClassNotFoundException("Simulated ClassNotFoundException");
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(new SerializationUtils());
    }

    // --- clone(T) ---
    @Test
    public void testCloneNull() {
        // True branch: object == null
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testCloneNonNull() {
        // False branch: object != null
        final String orig = "commons-lang";
        final String cloned = SerializationUtils.clone(orig);
        assertEquals(orig, cloned);
    }

    @Test
    public void testCloneExceptionOnWrite() {
        // SerializationException during serialize phase of clone
        assertThrows(SerializationException.class, () -> SerializationUtils.clone(new ThrowingWriteSerializable()));
    }

    @Test
    public void testCloneExceptionOnReadIOException() {
        // IOException during in.readObject() caught in clone
        assertThrows(SerializationException.class, () -> SerializationUtils.clone(new ThrowingReadSerializable()));
    }

    @Test
    public void testCloneExceptionOnReadClassNotFound() {
        // ClassNotFoundException during in.readObject() caught in clone
        assertThrows(SerializationException.class, () -> SerializationUtils.clone(new ClassNotFoundReadSerializable()));
    }

    // --- deserialize(byte[]) ---
    @Test
    public void testDeserializeByteArrayNull() {
        assertThrows(NullPointerException.class, () -> SerializationUtils.deserialize((byte[]) null));
    }

    @Test
    public void testDeserializeByteArrayValid() {
        final byte[] bytes = SerializationUtils.serialize("test-data");
        final String result = SerializationUtils.deserialize(bytes);
        assertEquals("test-data", result);
    }

    @Test
    public void testDeserializeByteArrayInvalid() {
        assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new byte[0]));
    }

    // --- deserialize(InputStream) ---
    @Test
    public void testDeserializeInputStreamNull() {
        assertThrows(NullPointerException.class, () -> SerializationUtils.deserialize((InputStream) null));
    }

    @Test
    public void testDeserializeInputStreamValid() {
        final byte[] bytes = SerializationUtils.serialize(Integer.valueOf(42));
        final Integer result = SerializationUtils.deserialize(new ByteArrayInputStream(bytes));
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testDeserializeInputStreamIOException() {
        assertThrows(SerializationException.class,
                () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[] { 1, 2, 3 })));
    }

    @Test
    public void testDeserializeInputStreamClassNotFound() {
        final byte[] bytes = SerializationUtils.serialize(new ClassNotFoundReadSerializable());
        assertThrows(SerializationException.class,
                () -> SerializationUtils.deserialize(new ByteArrayInputStream(bytes)));
    }

    @Test
    public void testDeserializeInputStreamNegativeArraySize() {
        // Byte array representing a stream triggering NegativeArraySizeException
        final byte[] byteArray = {
            (byte) -84, (byte) -19, (byte) 0, (byte) 5, (byte) 125, (byte) -19, (byte) 0,
            (byte) 5, (byte) 115, (byte) 114, (byte) -1, (byte) 97, (byte) 122, (byte) -48, (byte) -65
        };
        assertThrows(SerializationException.class,
                () -> SerializationUtils.deserialize(new ByteArrayInputStream(byteArray)));
    }

    // --- requireNonNull(T, String) ---
    @Test
    public void testRequireNonNull() throws InvalidObjectException {
        // True branch: obj == null
        final InvalidObjectException ex = assertThrows(InvalidObjectException.class,
                () -> SerializationUtils.requireNonNull(null, "Object cannot be null"));
        assertEquals("Object cannot be null", ex.getMessage());

        // False branch: obj != null
        final String obj = "valid";
        assertSame(obj, SerializationUtils.requireNonNull(obj, "message"));
    }

    // --- roundtrip(T) ---
    @Test
    public void testRoundtrip() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        final HashMap<String, String> result = SerializationUtils.roundtrip(map);
        assertEquals(map, result);

        assertNull(SerializationUtils.roundtrip(null));
    }

    // --- serialize(Serializable) ---
    @Test
    public void testSerializeNull() {
        final byte[] bytes = SerializationUtils.serialize(null);
        assertNotNull(bytes);
        assertNull(SerializationUtils.deserialize(bytes));
    }

    @Test
    public void testSerializeValid() {
        final byte[] bytes = SerializationUtils.serialize("hello");
        assertNotNull(bytes);
        assertEquals("hello", SerializationUtils.deserialize(bytes));
    }

    @Test
    public void testSerializeException() {
        assertThrows(SerializationException.class, () -> SerializationUtils.serialize(new ThrowingWriteSerializable()));
    }

    // --- serialize(Serializable, OutputStream) ---
    @Test
    public void testSerializeStreamNull() {
        assertThrows(NullPointerException.class, () -> SerializationUtils.serialize("hello", null));
    }

    @Test
    public void testSerializeStreamValid() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize("stream-test", baos);
        final String result = SerializationUtils.deserialize(baos.toByteArray());
        assertEquals("stream-test", result);
    }

    @Test
    public void testSerializeStreamIOException() {
        final OutputStream errorOut = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                throw new IOException("Stream write failed");
            }
        };
        assertThrows(SerializationException.class, () -> SerializationUtils.serialize("hello", errorOut));
    }

    // --- ClassLoaderAwareObjectInputStream CFG branch testing ---

    @Test
    public void testClassLoaderAwareObjectInputStreamFallbackToContextClassLoader() throws Exception {
        // Branch: Class.forName(name, false, classLoader) throws CNFE,
        // but Thread.currentThread().getContextClassLoader() resolves it.
        final byte[] bytes = SerializationUtils.serialize(Integer.valueOf(123));
        final ClassLoader dummyLoader = new ClassLoader(null) {
            @Override
            public Class<?> loadClass(final String name) throws ClassNotFoundException {
                throw new ClassNotFoundException("DummyLoader cannot load " + name);
            }
        };

        try (ClassLoaderAwareObjectInputStream in =
                     new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(bytes), dummyLoader)) {
            final Object obj = in.readObject();
            assertEquals(Integer.valueOf(123), obj);
        }
    }

    @Test
    public void testClassLoaderAwareObjectInputStreamPrimitiveTypes() {
        // Branch: cls != null is true (ClassUtils.getPrimitiveClass returns the primitive class)
        final Class<?>[] primitiveTypes = {
            byte.class, short.class, int.class, long.class,
            float.class, double.class, boolean.class, char.class, void.class
        };

        for (final Class<?> primitiveType : primitiveTypes) {
            final Class<?> cloned = SerializationUtils.clone(primitiveType);
            assertEquals(primitiveType, cloned);
        }
    }

    @Test
    public void testClassLoaderAwareObjectInputStreamClassNotFoundThrowsCnfe() throws Exception {
        // Branch: cls != null is false -> throw cnfe
        // Both custom classLoader and contextClassLoader fail to load the class,
        // and the class name is NOT a primitive type.
        final byte[] bytes = SerializationUtils.serialize(Integer.valueOf(999));
        final ClassLoader failingLoader = new ClassLoader(null) {
            @Override
            public Class<?> loadClass(final String name) throws ClassNotFoundException {
                throw new ClassNotFoundException("Blocked: " + name);
            }
        };

        final ClassLoader originalContext = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(failingLoader);
            try (ClassLoaderAwareObjectInputStream in =
                         new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(bytes), failingLoader)) {
                assertThrows(ClassNotFoundException.class, () -> in.readObject());
            }
        } finally {
            Thread.currentThread().setContextClassLoader(originalContext);
        }
    }
}
