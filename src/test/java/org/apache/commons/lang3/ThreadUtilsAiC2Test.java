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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Predicate;

import org.apache.commons.lang3.ThreadUtils.NamePredicate;
import org.apache.commons.lang3.ThreadUtils.ThreadGroupPredicate;
import org.apache.commons.lang3.ThreadUtils.ThreadIdPredicate;
import org.apache.commons.lang3.ThreadUtils.ThreadPredicate;
import org.junit.jupiter.api.Test;

/**
 * AI-generated C2 (Branch Coverage) test suite for {@link ThreadUtils}.
 */
@SuppressWarnings("deprecation")
public class ThreadUtilsAiC2Test {

    @Test
    public void testConstructor() {
        assertNotNull(new ThreadUtils());
    }

    @Test
    public void testAlwaysTruePredicate() {
        assertNotNull(ThreadUtils.ALWAYS_TRUE_PREDICATE);
        final ThreadPredicate threadPred = ThreadUtils.ALWAYS_TRUE_PREDICATE;
        final ThreadGroupPredicate groupPred = ThreadUtils.ALWAYS_TRUE_PREDICATE;
        assertTrue(threadPred.test((Thread) null));
        assertTrue(threadPred.test(Thread.currentThread()));
        assertTrue(groupPred.test((ThreadGroup) null));
        assertTrue(groupPred.test(Thread.currentThread().getThreadGroup()));
    }

    @Test
    public void testNamePredicate() {
        assertThrows(NullPointerException.class, () -> new NamePredicate(null));

        final String name = "test-name";
        final NamePredicate predicate = new NamePredicate(name);

        // Thread tests
        assertFalse(predicate.test((Thread) null));
        final Thread matchingThread = new Thread(name);
        assertTrue(predicate.test(matchingThread));
        final Thread nonMatchingThread = new Thread("other-name");
        assertFalse(predicate.test(nonMatchingThread));

        // ThreadGroup tests
        assertFalse(predicate.test((ThreadGroup) null));
        final ThreadGroup matchingGroup = new ThreadGroup(name);
        assertTrue(predicate.test(matchingGroup));
        final ThreadGroup nonMatchingGroup = new ThreadGroup("other-group");
        assertFalse(predicate.test(nonMatchingGroup));
    }

    @Test
    public void testThreadIdPredicate() {
        assertThrows(IllegalArgumentException.class, () -> new ThreadIdPredicate(0));
        assertThrows(IllegalArgumentException.class, () -> new ThreadIdPredicate(-1));

        final long currentId = Thread.currentThread().getId();
        final ThreadIdPredicate predicate = new ThreadIdPredicate(currentId);

        assertFalse(predicate.test(null));
        assertTrue(predicate.test(Thread.currentThread()));

        final Thread otherThread = new Thread("otherThread");
        assertFalse(predicate.test(otherThread));
    }

    @Test
    public void testFindThreadById() {
        assertThrows(IllegalArgumentException.class, () -> ThreadUtils.findThreadById(0));
        assertThrows(IllegalArgumentException.class, () -> ThreadUtils.findThreadById(-5));

        final Thread current = Thread.currentThread();
        final Thread found = ThreadUtils.findThreadById(current.getId());
        assertSame(current, found);

        final Thread notFound = ThreadUtils.findThreadById(Long.MAX_VALUE - 100);
        assertNull(notFound);
    }

    @Test
    public void testFindThreadByIdAndGroupName() {
        final Thread current = Thread.currentThread();
        final long id = current.getId();
        final String groupName = current.getThreadGroup().getName();

        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadById(id, (String) null));
        assertThrows(IllegalArgumentException.class, () -> ThreadUtils.findThreadById(0, groupName));

        // Matching thread and group name
        assertSame(current, ThreadUtils.findThreadById(id, groupName));

        // Matching thread but different group name
        assertNull(ThreadUtils.findThreadById(id, "non-existent-group-name-xyz"));

        // Non-existent thread id
        assertNull(ThreadUtils.findThreadById(Long.MAX_VALUE - 100, groupName));
    }

    @Test
    public void testFindThreadByIdAndThreadGroup() {
        final Thread current = Thread.currentThread();
        final long id = current.getId();
        final ThreadGroup group = current.getThreadGroup();

        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadById(id, (ThreadGroup) null));
        assertThrows(IllegalArgumentException.class, () -> ThreadUtils.findThreadById(0, group));

        // Matching thread and group
        assertSame(current, ThreadUtils.findThreadById(id, group));

        // Matching thread but different group
        final ThreadGroup otherGroup = new ThreadGroup("other-test-group");
        assertNull(ThreadUtils.findThreadById(id, otherGroup));

        // Non-existent thread id
        assertNull(ThreadUtils.findThreadById(Long.MAX_VALUE - 100, group));
    }

    @Test
    public void testFindThreadGroups() {
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadGroups((Predicate<ThreadGroup>) null));
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadGroups((ThreadGroup) null, true, (Predicate<ThreadGroup>) t -> true));
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadGroups(Thread.currentThread().getThreadGroup(), true, (Predicate<ThreadGroup>) null));

        final Collection<ThreadGroup> allGroups = ThreadUtils.findThreadGroups((Predicate<ThreadGroup>) t -> true);
        assertNotNull(allGroups);
        assertFalse(allGroups.isEmpty());

        final Collection<ThreadGroup> none = ThreadUtils.findThreadGroups((Predicate<ThreadGroup>) t -> false);
        assertNotNull(none);
        assertTrue(none.isEmpty());

        final ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        final Collection<ThreadGroup> nonRecurse = ThreadUtils.findThreadGroups(currentGroup, false, (Predicate<ThreadGroup>) t -> true);
        assertNotNull(nonRecurse);

        // Deprecated overloads with ThreadGroupPredicate
        final Collection<ThreadGroup> deprecatedAll = ThreadUtils.findThreadGroups((ThreadGroupPredicate) t -> true);
        assertNotNull(deprecatedAll);
        assertFalse(deprecatedAll.isEmpty());

        final Collection<ThreadGroup> deprecatedGroup = ThreadUtils.findThreadGroups(currentGroup, true, (ThreadGroupPredicate) t -> true);
        assertNotNull(deprecatedGroup);
    }

    @Test
    public void testFindThreadGroupsByName() {
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadGroupsByName(null));

        final String currentGroupName = Thread.currentThread().getThreadGroup().getName();
        final Collection<ThreadGroup> groups = ThreadUtils.findThreadGroupsByName(currentGroupName);
        assertNotNull(groups);
        assertFalse(groups.isEmpty());

        final Collection<ThreadGroup> none = ThreadUtils.findThreadGroupsByName("non-existent-group-name-xyz");
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    @Test
    public void testFindThreads() {
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreads((Predicate<Thread>) null));
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreads((ThreadGroup) null, true, (Predicate<Thread>) t -> true));
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreads(Thread.currentThread().getThreadGroup(), true, (Predicate<Thread>) null));

        final Collection<Thread> all = ThreadUtils.findThreads((Predicate<Thread>) t -> true);
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertTrue(all.contains(Thread.currentThread()));

        final Collection<Thread> none = ThreadUtils.findThreads((Predicate<Thread>) t -> false);
        assertNotNull(none);
        assertTrue(none.isEmpty());

        final ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        final Collection<Thread> subThreads = ThreadUtils.findThreads(currentGroup, false, (Predicate<Thread>) t -> true);
        assertNotNull(subThreads);

        // Deprecated overloads with ThreadPredicate
        final Collection<Thread> deprecatedAll = ThreadUtils.findThreads((ThreadPredicate) t -> true);
        assertNotNull(deprecatedAll);
        assertFalse(deprecatedAll.isEmpty());

        final Collection<Thread> deprecatedGroup = ThreadUtils.findThreads(currentGroup, true, (ThreadPredicate) t -> true);
        assertNotNull(deprecatedGroup);
    }

    @Test
    public void testFindThreadsByName() {
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadsByName(null));

        final String currentName = Thread.currentThread().getName();
        final Collection<Thread> threads = ThreadUtils.findThreadsByName(currentName);
        assertNotNull(threads);
        assertFalse(threads.isEmpty());
        assertTrue(threads.contains(Thread.currentThread()));

        final Collection<Thread> none = ThreadUtils.findThreadsByName("non-existent-thread-name-xyz");
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    @Test
    public void testFindThreadsByNameAndGroupName() {
        final Thread current = Thread.currentThread();
        final String currentName = current.getName();
        final String currentGroupName = current.getThreadGroup().getName();

        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadsByName(null, currentGroupName));
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadsByName(currentName, (String) null));

        final Collection<Thread> found = ThreadUtils.findThreadsByName(currentName, currentGroupName);
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertTrue(found.contains(current));

        final Collection<Thread> notFound = ThreadUtils.findThreadsByName("non-existent-thread-xyz", currentGroupName);
        assertNotNull(notFound);
        assertTrue(notFound.isEmpty());

        final Collection<Thread> wrongGroup = ThreadUtils.findThreadsByName(currentName, "non-existent-group-xyz");
        assertNotNull(wrongGroup);
        assertTrue(wrongGroup.isEmpty());
    }

    @Test
    public void testFindThreadsByNameAndThreadGroup() {
        final Thread current = Thread.currentThread();
        final String currentName = current.getName();
        final ThreadGroup group = current.getThreadGroup();

        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadsByName(null, group));
        assertThrows(NullPointerException.class, () -> ThreadUtils.findThreadsByName(currentName, (ThreadGroup) null));

        final Collection<Thread> found = ThreadUtils.findThreadsByName(currentName, group);
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertTrue(found.contains(current));

        final Collection<Thread> notFound = ThreadUtils.findThreadsByName("non-existent-thread-xyz", group);
        assertNotNull(notFound);
        assertTrue(notFound.isEmpty());
    }

    @Test
    public void testGetAllThreadGroupsAndGetAllThreads() {
        final Collection<ThreadGroup> groups = ThreadUtils.getAllThreadGroups();
        assertNotNull(groups);

        final Collection<Thread> threads = ThreadUtils.getAllThreads();
        assertNotNull(threads);
        assertFalse(threads.isEmpty());
        assertTrue(threads.contains(Thread.currentThread()));
    }

    @Test
    public void testGetSystemThreadGroup() {
        final ThreadGroup systemGroup = ThreadUtils.getSystemThreadGroup();
        assertNotNull(systemGroup);
        assertNull(systemGroup.getParent());
    }

    @Test
    public void testJoin() throws InterruptedException {
        final Thread t = new Thread(() -> {});
        t.start();
        ThreadUtils.join(t, Duration.ofMillis(100));
        assertFalse(t.isAlive());
    }

    @Test
    public void testSleep() throws InterruptedException {
        ThreadUtils.sleep(Duration.ofMillis(10));
    }

    @Test
    public void testSleepQuietly() {
        // Normal sleep
        ThreadUtils.sleepQuietly(Duration.ofMillis(10));

        // Interrupted sleep
        Thread.currentThread().interrupt();
        ThreadUtils.sleepQuietly(Duration.ofMillis(200));
        assertTrue(Thread.interrupted());
    }
}
