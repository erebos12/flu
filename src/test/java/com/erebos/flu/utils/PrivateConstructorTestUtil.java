package com.erebos.flu.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class PrivateConstructorTestUtil {

    public static <T> void testPrivateConstructor(Class<T> clazz) throws NoSuchMethodException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true); // Make the private constructor accessible

        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance, // Attempt to instantiate the utility class
                "Expected an IllegalStateException when instantiating " + clazz.getName()
        );

        assertTrue(
                exception.getCause() instanceof IllegalStateException,
                "Cause should be an IllegalStateException"
        );
        assertEquals(
                "Utility class",
                exception.getCause().getMessage(),
                "Exception message should match"
        );
    }
}
