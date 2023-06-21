package com.erebos.flu.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

public class HashUtils {

    private HashUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String perfectHash(final String s) {
        try {
            MessageDigest md = getInstance("SHA-256");
            md.update(s.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error during hashing!");
        }
    }
}
