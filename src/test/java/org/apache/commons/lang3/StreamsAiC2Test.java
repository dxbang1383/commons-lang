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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.Streams.ArrayCollector;
import org.apache.commons.lang3.Streams.FailableStream;
import org.junit.jupiter.api.Test;

/**
 * AI-generated C2 (Branch Coverage) test suite for {@link Streams}.
 */
@SuppressWarnings("deprecation")
public class StreamsAiC2Test {

    @Test
    public void testConstructorAndStaticMethods() {
        assertNotNull(new Streams());

        final FailableStream<String> sFromCollection = Streams.stream(Arrays.asList("a", "b"));
        assertNotNull(sFromCollection);

        final FailableStream<String> sFromStream = Streams.stream(Stream.of("a", "b"));
        assertNotNull(sFromStream);

        final Collector<String, ?, String[]> arrayCollector = Streams.toArray(String.class);
        assertNotNull(arrayCollector);
    }

    @Test
    public void testArrayCollector() {
        final ArrayCollector<String> collector = new ArrayCollector<>(String.class);
        assertNotNull(collector.characteristics());
        assertTrue(collector.characteristics().isEmpty());

        final List<String> list = collector.supplier().get();
        assertNotNull(list);
        assertEquals(0, list.size());

        collector.accumulator().accept(list, "one");
        assertEquals(1, list.size());
        assertEquals("one", list.get(0));

        final List<String> list2 = new ArrayList<>();
        list2.add("two");
        final List<String> combined = collector.combiner().apply(list, list2);
        assertSame(list, combined);
        assertEquals(2, combined.size());

        final String[] array = collector.finisher().apply(combined);
        assertNotNull(array);
        assertArrayEquals(new String[] {"one", "two"}, array);

        // Through stream collect
        final String[] result = Stream.of("foo", "bar").collect(Streams.toArray(String.class));
        assertArrayEquals(new String[] {"foo", "bar"}, result);
    }

    @Test
    public void testFailableStreamAllMatch() {
        // Empty stream -> always true
        assertTrue(Streams.stream(Collections.<String>emptyList()).allMatch(s -> false));

        // Non-empty match all
        assertTrue(Streams.stream(Arrays.asList("apple", "apricot")).allMatch(s -> s.startsWith("a")));

        // Non-empty mismatch
        assertFalse(Streams.stream(Arrays.asList("apple", "banana")).allMatch(s -> s.startsWith("a")));

        // Failable predicate throwing checked exception
        assertThrows(UncheckedIOException.class, () ->
            Streams.stream(Arrays.asList("a")).allMatch(s -> {
                throw new IOException("boom");
            })
        );
    }

    @Test
    public void testFailableStreamAnyMatch() {
        // Empty stream -> always false
        assertFalse(Streams.stream(Collections.<String>emptyList()).anyMatch(s -> true));

        // Non-empty matches
        assertTrue(Streams.stream(Arrays.asList("banana", "apple")).anyMatch(s -> s.startsWith("a")));

        // Non-empty none matches
        assertFalse(Streams.stream(Arrays.asList("banana", "cherry")).anyMatch(s -> s.startsWith("a")));

        // Failable predicate throwing checked exception
        assertThrows(UncheckedIOException.class, () ->
            Streams.stream(Arrays.asList("a")).anyMatch(s -> {
                throw new IOException("boom");
            })
        );
    }

    @Test
    public void testFailableStreamFilter() {
        final List<String> filtered = Streams.stream(Arrays.asList("a", "bb", "ccc"))
            .filter(s -> s.length() > 1)
            .collect(Collectors.toList());
        assertEquals(Arrays.asList("bb", "ccc"), filtered);

        assertThrows(UncheckedIOException.class, () ->
            Streams.stream(Arrays.asList("a"))
                .filter(s -> {
                    throw new IOException("fail filter");
                })
                .collect(Collectors.toList())
        );
    }

    @Test
    public void testFailableStreamMap() {
        final List<Integer> mapped = Streams.stream(Arrays.asList("1", "22", "333"))
            .map(String::length)
            .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3), mapped);

        assertThrows(UncheckedIOException.class, () ->
            Streams.stream(Arrays.asList("a"))
                .map(s -> {
                    throw new IOException("fail map");
                })
                .collect(Collectors.toList())
        );
    }

    @Test
    public void testFailableStreamForEach() {
        final List<String> result = new ArrayList<>();
        Streams.stream(Arrays.asList("x", "y", "z")).forEach(result::add);
        assertEquals(Arrays.asList("x", "y", "z"), result);

        assertThrows(UncheckedIOException.class, () ->
            Streams.stream(Arrays.asList("x")).forEach(s -> {
                throw new IOException("fail forEach");
            })
        );
    }

    @Test
    public void testFailableStreamReduce() {
        final String reduced = Streams.stream(Arrays.asList("a", "b", "c"))
            .reduce("", (s1, s2) -> s1 + s2);
        assertEquals("abc", reduced);
    }

    @Test
    public void testFailableStreamCollectThreeArgs() {
        final StringBuilder sb = Streams.stream(Arrays.asList("a", "b", "c"))
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
        assertEquals("abc", sb.toString());
    }

    @Test
    public void testFailableStreamStreamAccessor() {
        final Stream<String> stream = Stream.of("hello");
        final FailableStream<String> failable = Streams.stream(stream);
        assertSame(stream, failable.stream());
    }

    @Test
    public void testTerminatedStreamBranches() {
        // Test termination via collect(Collector)
        final FailableStream<String> fs1 = Streams.stream(Arrays.asList("a", "b"));
        fs1.collect(Collectors.toList());
        assertThrows(IllegalStateException.class, () -> fs1.assertNotTerminated());
        assertThrows(IllegalStateException.class, () -> fs1.allMatch(s -> true));
        assertThrows(IllegalStateException.class, () -> fs1.anyMatch(s -> true));
        assertThrows(IllegalStateException.class, () -> fs1.filter(s -> true));
        assertThrows(IllegalStateException.class, () -> fs1.map(s -> s));
        assertThrows(IllegalStateException.class, () -> fs1.forEach(s -> {}));
        assertThrows(IllegalStateException.class, () -> fs1.collect(Collectors.toList()));
        assertThrows(IllegalStateException.class, () -> fs1.collect(ArrayList::new, List::add, List::addAll));
        assertThrows(IllegalStateException.class, () -> fs1.reduce("", (a, b) -> a + b));
        assertThrows(IllegalStateException.class, () -> fs1.makeTerminated());

        // Test termination via forEach
        final FailableStream<String> fs2 = Streams.stream(Arrays.asList("a", "b"));
        fs2.forEach(s -> {});
        assertThrows(IllegalStateException.class, () -> fs2.assertNotTerminated());

        // Test termination via reduce
        final FailableStream<String> fs3 = Streams.stream(Arrays.asList("a", "b"));
        fs3.reduce("", (a, b) -> a + b);
        assertThrows(IllegalStateException.class, () -> fs3.assertNotTerminated());

        // Test termination via collect three args
        final FailableStream<String> fs4 = Streams.stream(Arrays.asList("a", "b"));
        fs4.collect(ArrayList::new, List::add, List::addAll);
        assertThrows(IllegalStateException.class, () -> fs4.assertNotTerminated());
    }
}
