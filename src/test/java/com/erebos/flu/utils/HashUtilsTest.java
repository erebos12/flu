package com.erebos.flu.utils;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.erebos.flu.utils.HashUtils.myHashCode;
import static com.erebos.flu.utils.HashUtils.perfectHash;
import static com.erebos.flu.utils.PrivateConstructorTestUtil.testPrivateConstructor;
import static org.junit.jupiter.api.Assertions.*;

class HashUtilsTest {

    @Test
    void testConstructorThrowsException() throws NoSuchMethodException {
        testPrivateConstructor(HashUtils.class);
    }

    @Test
    void testHashFunction() {
        String s = "Alexander";
        String hashed = perfectHash(s);
        assertEquals("dbadfc88144b0c153a2d1bdf154681c857a237eb79d58df24e918bca6e17db05", hashed);
        assertEquals(64, hashed.length());

        long begin = System.nanoTime();
        s = "This is a longer string which will be hashed and has special characters &%.-?";
        hashed = perfectHash(s);
        long end = System.nanoTime();
        long timeInMsecs = (end - begin) / 1000000;
        assertTrue(timeInMsecs < 10);
        assertEquals("e03b8510eb31a156cb34a68a35564ea67da1ee461c6d6363d0221938b3e34d04", hashed);
        assertEquals(64, hashed.length());

        s = "PC1CC2JID2";
        hashed = perfectHash(s);
        assertEquals("813e2855b4cd0a13a987ae775b324dfab5a3ee991f00e46bfdea9be1209ed012", hashed);
        assertEquals(64, hashed.length());
    }

    @Test
    void bulkTest() {
        int MAX = 1000000;
        Set<String> hashes = new HashSet<>();
        for (int i = 1; i <= MAX; i++) {
            String pc = "ProfitCenter" + i;
            String cc = "CostCenter" + i;
            String jid = "JobID" + i;
            String hash = perfectHash(pc + cc + jid);
            if (hashes.contains(hash)) {
                throw new IllegalStateException("hashes must be unique");
            } else {
                hashes.add(hash);
            }
        }
        assertEquals(MAX, hashes.size());
    }

}
