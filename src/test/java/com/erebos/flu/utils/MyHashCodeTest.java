package com.erebos.flu.utils;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.erebos.flu.utils.HashUtils.myHashCode;
import static com.erebos.flu.utils.HashUtils.perfectHash;
import static com.erebos.flu.utils.PrivateConstructorTestUtil.testPrivateConstructor;
import static org.junit.jupiter.api.Assertions.*;

class MyHashCodeTest {

    @Test
    void testConstructorThrowsException() throws NoSuchMethodException {
        testPrivateConstructor(HashUtils.class);
    }


    @Test
    void test_my_hash_code_always_same() {
        // myHashCode() must always produce the same value
        var str1 = "Alexander";
        var str2 = "Alexander";
        assertEquals(1025299764, myHashCode("Alexander"));
        // myHashCode() is idempotent and must produce the same value for equal strings
        assertEquals(myHashCode(str1), myHashCode(str2));
    }

    // Returns 0 for empty string input
    @Test
    public void test_returns_zero_for_empty_string() {
        String input = "";
        int hash = myHashCode(input);
        assertEquals(0, hash);
    }

    @Test
    void testHashStableForSameInput() {
        assertEquals(myHashCode("test"), myHashCode("test"), "Hash should be stable for the same input");
    }

    @Test
    void testHashDifferentForCaseChange() {
        assertNotEquals(myHashCode("test"), myHashCode("Test"), "Hashes should be different for different cases");
    }

    @Test
    void testHashForNullInput() {
        assertEquals(0, myHashCode(null), "Null input should return 0");
    }

    @Test
    void testHashDifferentForSimilarPrefixes() {
        assertNotEquals(myHashCode("abc"), myHashCode("bc"), "Hash of 'abc' should not be similar to 'bc'");
    }

    @Test
    void testHashIsAlwaysPositive() {
        for (int i = 0; i < 1000; i++) {
            int hash = myHashCode("random" + i);
            assertTrue(hash >= 0, "Hash should always be positive");
        }
    }
}
