package com.erebos.flu.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

/**
 * Utility class providing helper methods for hashing operations.
 * This class contains methods for generating secure hashes of strings.
 */
public class HashUtils {

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private HashUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Generates a SHA-256 hash of the input string.
     * The hash is returned as a 64-character hexadecimal string.
     *
     * @param s the string to hash
     * @return the SHA-256 hash as a hexadecimal string
     * @throws IllegalStateException if the SHA-256 algorithm is not available
     */
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
