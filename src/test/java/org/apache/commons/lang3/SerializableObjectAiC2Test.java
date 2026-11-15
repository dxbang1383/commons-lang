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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

/**
 * AI-generated C2 (Branch Coverage) test suite for {@link SerializableObject}.
 */
public class SerializableObjectAiC2Test {

    @Test
    public void testConstructor() {
        final SerializableObject obj = new SerializableObject();
        assertNotNull(obj);
    }

    @Test
    public void testSerialization() throws Exception {
        final SerializableObject original = new SerializableObject();

        final byte[] bytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
            oos.flush();
            bytes = baos.toByteArray();
        }

        assertTrue(bytes.length > 0);

        final Object deserialized;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserialized = ois.readObject();
        }

        assertNotNull(deserialized);
        assertTrue(deserialized instanceof SerializableObject);
    }

    @Test
    public void testSynchronizationLock() {
        final SerializableObject lock = new SerializableObject();
        synchronized (lock) {
            assertNotNull(lock);
        }
    }
}
