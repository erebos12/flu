package com.erebos.flu.utils;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.erebos.flu.utils.LambdaUtils.ifAvailableOrElse;
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

    // Custom object for testing
    static class Person {
        private final String name;
        private final int age;

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

