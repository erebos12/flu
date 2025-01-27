package com.erebos.flu.utils;

import com.erebos.flu.utils.pojo.Person;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.erebos.flu.utils.LambdaUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class LambdaUtilsTest {

    @Test
    void testIfAvailableOrElse_WithString() {
        // Arrange
        String value = "Hello, World!";
        AtomicBoolean ifCaseExecuted = new AtomicBoolean(false);
        AtomicBoolean elseCaseExecuted = new AtomicBoolean(false);

        // Act
        ifAvailableOrElse(
                value,
                v -> {
                    assertEquals("Hello, World!", v);
                    ifCaseExecuted.set(true);
                },
                () -> elseCaseExecuted.set(true)
        );

        // Assert
        assertTrue(ifCaseExecuted.get(), "IF-case should be executed");
        assertFalse(elseCaseExecuted.get(), "ELSE-case should not be executed");
    }

    @Test
    void testIfAvailableOrElse_WithList() {
        // Arrange
        List<String> list = Arrays.asList("A", "B", "C");
        AtomicBoolean ifCaseExecuted = new AtomicBoolean(false);
        AtomicBoolean elseCaseExecuted = new AtomicBoolean(false);

        // Act
        ifAvailableOrElse(
                list,
                l -> {
                    assertEquals(3, l.size());
                    assertTrue(l.contains("B"));
                    ifCaseExecuted.set(true);
                },
                () -> elseCaseExecuted.set(true)
        );

        // Assert
        assertTrue(ifCaseExecuted.get(), "IF-case should be executed");
        assertFalse(elseCaseExecuted.get(), "ELSE-case should not be executed");
    }

    @Test
    void testIfAvailableOrElse_WithMap() {
        // Arrange
        Map<String, Integer> map = new HashMap<>();
        map.put("Key1", 1);
        map.put("Key2", 2);
        AtomicBoolean ifCaseExecuted = new AtomicBoolean(false);
        AtomicBoolean elseCaseExecuted = new AtomicBoolean(false);

        // Act
        ifAvailableOrElse(
                map,
                m -> {
                    assertEquals(2, m.size());
                    assertTrue(m.containsKey("Key1"));
                    ifCaseExecuted.set(true);
                },
                () -> elseCaseExecuted.set(true)
        );

        // Assert
        assertTrue(ifCaseExecuted.get(), "IF-case should be executed");
        assertFalse(elseCaseExecuted.get(), "ELSE-case should not be executed");
    }

    @Test
    void testIfAvailableOrElse_WithCustomObject() {
        // Arrange
        Person person = new Person("John", 30);
        AtomicBoolean ifCaseExecuted = new AtomicBoolean(false);
        AtomicBoolean elseCaseExecuted = new AtomicBoolean(false);

        // Act
        ifAvailableOrElse(
                person,
                p -> {
                    assertEquals("John", p.getName());
                    assertEquals(30, p.getAge());
                    ifCaseExecuted.set(true);
                },
                () -> elseCaseExecuted.set(true)
        );

        // Assert
        assertTrue(ifCaseExecuted.get(), "IF-case should be executed");
        assertFalse(elseCaseExecuted.get(), "ELSE-case should not be executed");
    }

    @Test
    void testIfAvailableOrElse_WithNullValue() {
        // Arrange
        Object nullValue = null;
        AtomicBoolean ifCaseExecuted = new AtomicBoolean(false);
        AtomicBoolean elseCaseExecuted = new AtomicBoolean(false);

        // Act
        ifAvailableOrElse(
                nullValue,
                v -> ifCaseExecuted.set(true),
                () -> elseCaseExecuted.set(true)
        );

        // Assert
        assertFalse(ifCaseExecuted.get(), "IF-case should not be executed");
        assertTrue(elseCaseExecuted.get(), "ELSE-case should be executed");
    }

    @Test
    void testIfAvailableOrElseMutable_ValuePresent_ModifiedInIfCase() {
        // given
        String input = "test";

        // when
        String result = ifAvailableOrElseMutable(
                input,
                i -> i.set(i.get().toUpperCase()),
                i -> i.set("default")
        );

        // then
        assertEquals("TEST", result);
    }

    @Test
    void testIfAvailableOrElseMutable_ValueNull_ModifiedInElseCase() {
        // given
        String input = null;

        // when
        String result = ifAvailableOrElseMutable(
                input,
                i -> i.set(i.get().toUpperCase()),
                i -> i.set("default")
        );

        // then
        assertEquals("default", result);
    }

    @Test
    void testIfAvailableOrElseMutable_ValuePresent_NoModification() {
        // given
        String input = "test";

        // when
        String result = ifAvailableOrElseMutable(
                input,
                i -> {
                }, // do nothing
                i -> i.set("default")
        );

        // then
        assertEquals("test", result);
    }

    @Test
    void testIfAvailableOrElseMutable_NoModification() {
        // given
        Person input = new Person("Alex", 45);

        // when
        Person p = ifAvailableOrElseMutable(
                input,
                i -> {
                    i.set(new Person("Alex Sahm", 18));
                },
                i -> {
                }  // do nothing
        );

        // then
        assertEquals(p.name, "Alex Sahm");
        assertEquals(p.age, 18);
    }

    @Test
    void testIfAvailableOrElseMutable_ValuePresent_ComplexModification() {
        // given
        Integer input = 5;

        // when
        Integer result = ifAvailableOrElseMutable(
                input,
                i -> i.set(i.get() * 2),
                i -> i.set(0)
        );

        // then
        assertEquals(10, result);
    }

    @Test
    void testIfAvailableOrElseMutable_ValueNull_ComplexModification() {
        // given
        Integer input = null;

        // when
        Integer result = LambdaUtils.ifAvailableOrElseMutable(
                input,
                holder -> holder.set(holder.get() * 2),
                holder -> holder.set(42)
        );

        // then
        assertEquals(42, result);
    }

    // Custom object for testing
    static class Person {
        private String name;
        private int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
